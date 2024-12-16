package io.verse.storage.core.sql

import io.tagd.core.Releasable
import io.verse.storage.core.sql.model.SQLiteTable
import io.verse.storage.core.sql.model.binding.SQLiteColumnBinding
import io.verse.storage.core.sql.model.binding.SQLiteRowBinding

interface TableBinding<T> : Releasable {

    val tableName: String

    val table: SQLiteTable

    fun initWith(table: SQLiteTable)

    fun bindRecord(record: T): SQLiteRowBinding

    fun bindColumn(columnName: String, value: Any?): SQLiteColumnBinding {
        val binder = table.bind(columnName, value)
        return SQLiteColumnBinding(columnName, binder)
    }

    /**
     * A set of values of keys - which would uniquely identifies a record
     */
    fun ckColumnValueSet(record: T): Array<String>

    fun getRecord(cursor: Cursor): T

    override fun release() {
        //no-op
    }
}