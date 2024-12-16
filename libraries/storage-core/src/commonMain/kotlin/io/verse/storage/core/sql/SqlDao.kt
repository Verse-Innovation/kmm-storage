package io.verse.storage.core.sql

import io.tagd.arch.data.dao.AbstractDao
import io.tagd.arch.datatype.CrudOperation
import io.tagd.arch.datatype.DataObjectable
import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.arch.domain.crosscutting.async.diskIO
import io.tagd.langx.Callback
import io.tagd.di.Scope
import io.tagd.langx.IllegalAccessException
import io.verse.storage.core.sql.model.SQLiteTable
import io.verse.storage.core.sql.model.binding.SQLiteRowBinding
import io.verse.storage.core.sql.model.binding.SQLiteWhereClauseBinding
import io.verse.storage.core.sqliteDatabase
import io.verse.storage.core.sqliteTable

abstract class SqlDao<T : DataObjectable>(
    protected val scope: Scope,
    protected val databaseName: String,
    protected val binding: TableBinding<T>
) : AbstractDao(), AsyncContext {

    protected val tableName
        get() = binding.tableName

    protected val database: SQLiteDatabase?
        get() = sqliteDatabase(databaseName, scope)

    protected val table: SQLiteTable?
        get() = sqliteTable(tableName, databaseName, scope)

    init {
        binding.initWith(table!!)
    }

    open fun createAsync(rows: List<T>, callback: Callback<Int>) {
        diskIO {
            var count = 0
            transact {
                for (row in rows) {
                    createSynchronous(row)
                    count += 1
                }
                count
            }
            callback.invoke(count)
        }
    }

    open fun createAsync(row: T, callback: Callback<Int>) {
        if (row.crudOperation == CrudOperation.DELETE) {
            throw IllegalAccessException("$row with crudOperation DELETE is trying to insert")
        }

        diskIO {
            val id = transact {
                createSynchronous(row)
            }
            callback.invoke(id)
        }
    }

    open fun createSynchronous(row: T): Int {
        if (row.crudOperation != CrudOperation.CREATE) {
            throw IllegalAccessException("the crud operation should not be ${row.crudOperation}")
        }

        val rowBinding = bindRow(row)
        val query = "INSERT INTO $tableName (${rowBinding.columnCsv}) " +
                "VALUES (${rowBinding.placeholderCsv})"

        return database?.let { database ->
            val statement = database.compileStatement(query)

            for ((index, columnBinding) in rowBinding.columnBindings.withIndex()) {
                columnBinding.binder.bind(statement, index + 1)
            }

            val id = statement.executeInsert().toInt()

            statement.clearBindings()
            statement.close()

            id
        } ?: -1
    }

    open fun bindRow(row: T): SQLiteRowBinding {
        return binding.bindRecord(row)
    }

    open fun updateAsync(rows: List<T>, callback: Callback<Int>) {
        diskIO {
            var count = 0
            transact {
                for (row in rows) {
                    updateSynchronous(row)
                    count += 1
                }
                count
            }
            callback.invoke(count)
        }
    }

    open fun updateAsync(row: T, callback: Callback<Int>) {
        diskIO {
            val id = transact {
                updateSynchronous(row)
            }
            callback.invoke(id)
        }
    }

    open fun updateSynchronous(row: T): Int {
        deleteSynchronous(row)
        return createSynchronous(row)
    }

    open fun deleteAsync(ckColumnValuesList: List<Array<out String>>, callback: Callback<Int>) {
        diskIO {
            var count = 0
            transact {
                for (ckColumnValues in ckColumnValuesList) {
                    count += deleteSynchronous(ckColumnValues)
                }
                count
            }
            callback.invoke(count)
        }
    }

    open fun deleteAsync(vararg ckColumnValues: String, callback: Callback<Int>) {
        diskIO {
            val id = transact {
                deleteSynchronous(ckColumnValues)
            }
            callback.invoke(id)
        }
    }

    open fun deleteAsync(
        whereClause: String?,
        whereArgs: Array<String?>?,
        callback: Callback<Int>,
    ) {

        diskIO {
            val id = transact {
                deleteSynchronous(whereClause, whereArgs)
            }
            callback.invoke(id)
        }
    }

    open fun deleteWhereInAsync(
        fieldName: String,
        values: List<String?>,
        callback: Callback<Int>,
    ) {

        diskIO {
            val id = transact {
                deleteWhereIn(fieldName, values)
            }
            callback.invoke(id)
        }
    }

    open fun deleteWhereIn(fieldName: String, values: List<String?>): Int {
        val placeHoldersCsv = SQLiteRowBinding.placeHoldersCsv(values.size)
        val whereClause = "$fieldName IN ($placeHoldersCsv)"
        return database?.delete(tableName, whereClause, values.toTypedArray()) ?: -1
    }

    open fun deleteSynchronous(record: T): Int {
        val ckColumnValues = binding.ckColumnValueSet(record)
        val whereBinding = table!!.bindCompositeKeyForResultSet(ckColumnValues)
        return deleteSynchronous(whereBinding.clause, whereBinding.args)
    }

    open fun deleteSynchronous(columnValues: Array<out String>): Int {
        val whereBinding = table!!.bindCompositeKeyForResultSet(columnValues)
        return deleteSynchronous(whereBinding.clause, whereBinding.args)
    }

    open fun deleteSynchronous(whereClause: String?, whereArgs: Array<String?>?): Int {
        return database?.delete(tableName, whereClause, whereArgs) ?: -1
    }

    open fun deleteCascadeAsync(vararg ckColumnValues: String, callback: Callback<Int>) {
        diskIO {
            val id = transact {
                deleteCascadeSynchronous(*ckColumnValues)
            }
            callback.invoke(id)
        }
    }

    open fun deleteCascadeSynchronous(vararg ckColumnValues: String): Int {
        return deleteSynchronous(ckColumnValues)
    }

    protected fun transact(query: () -> Int): Int {
        database?.beginTransaction()
        val id: Int
        try {
            id = query.invoke()
            database?.setTransactionSuccessful()
        } finally {
            database?.endTransaction()
        }
        return id
    }

    open fun getAllAsync(callback: Callback<List<T>>) {
        getAllAsync(null, null, callback)
    }

    open fun getAllAsync(
        whereClause: String?,
        whereArgs: Array<String?>?,
        callback: Callback<List<T>>
    ) {

        diskIO {
            getAllSynchronous(whereClause, whereArgs, callback)    
        }
    }

    open fun getAllSynchronous(
        whereClause: String?,
        whereArgs: Array<String?>?,
        callback: Callback<List<T>>
    ) {
        
        val where = if (!whereClause.isNullOrEmpty()) {
            "WHERE $whereClause"
        } else {
            ""
        }

        database?.let { database ->
            val cursor = database.rawQuery("SELECT * FROM $tableName $where", whereArgs)
            var dtoList: ArrayList<T> = ArrayList()
            if (cursor.getCount() > 0) {
                dtoList = ArrayList()
                while (cursor.moveToNext()) {
                    dtoList.add(getRecord(cursor))
                }
            }
            cursor.close()
            callback.invoke(dtoList)
        }

    }

    open fun getAsync(vararg ckColumnValues: String, callback: Callback<T?>) {
        diskIO {
            val result = getSynchronous(ckColumnValues)
            callback.invoke(result)
        }
    }

    open fun getSynchronous(ckColumnValues: Array<out String>): T? {
        val whereBinding = table!!.bindCompositeKeyForResultSet(ckColumnValues)
        return getSynchronous(whereBinding.clause, whereBinding.args)
    }

    open fun getAsync(
        whereClause: String?,
        whereArgs: Array<String?>?,
        callback: Callback<T?>
    ) {

        diskIO {
            val result = getSynchronous(whereClause, whereArgs)
            callback.invoke(result)
        }
    }

    open fun getSynchronous(
        whereClause: String?,
        whereArgs: Array<String?>?,
    ): T? {

        val where = if (!whereClause.isNullOrEmpty()) {
            "WHERE $whereClause"
        } else {
            ""
        }

        return database?.let { database ->
            val cursor = database.rawQuery("SELECT * FROM $tableName $where", whereArgs)
            var dto: T? = null
            if (cursor.moveToFirst()) {
                dto = getRecord(cursor)
            }
            cursor.close()
            dto
        }
    }

    open fun getRecord(cursor: Cursor): T {
        return binding.getRecord(cursor)
    }

    open fun getCountAsync(
        whereClause: String?,
        whereArgs: Array<String?>?,
        callback: Callback<Int>
    ) {

        diskIO {
            val result = getCountSynchronous(whereClause, whereArgs)
            callback.invoke(result)
        }
    }

    open fun getCountSynchronous(columns: Array<out String>, whereArgs: Array<String?>?): Int {
        val whereClauseBinding = SQLiteWhereClauseBinding(columns.toList(), whereArgs?.toList())
        return getCountSynchronous(whereClauseBinding.clause, whereClauseBinding.args)
    }

    open fun getCountSynchronous(whereClause: String?, whereArgs: Array<String?>?): Int {
        val where = if (!whereClause.isNullOrEmpty()) {
            "WHERE $whereClause"
        } else {
            ""
        }

        return database?.let { database ->
            val cursor = database.rawQuery("SELECT * FROM $tableName $where", whereArgs)
            val result: Int = cursor.getCount()
            cursor.close()
            result
        } ?: 0
    }

    override fun release() {
        binding.release()
        super.release()
    }
}