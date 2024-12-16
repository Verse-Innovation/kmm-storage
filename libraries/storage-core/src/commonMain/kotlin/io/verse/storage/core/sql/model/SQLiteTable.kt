package io.verse.storage.core.sql.model

import io.tagd.arch.datatype.DataObject
import io.tagd.core.ValidateException
import io.tagd.langx.IllegalAccessException
import io.tagd.langx.isNull
import io.verse.storage.core.sql.StatementColumnBinder
import io.verse.storage.core.sql.model.binding.SQLiteWhereClauseBinding

data class SQLiteTable(
    val name: String,
    val columns: LinkedHashMap<String, SQLiteColumn>,
) : DataObject() {

    fun primaryKey(): Map<String, SQLiteColumn> {
        return columns.filter { it.value.primary }
    }

    fun foreignKey(): Map<String, SQLiteColumn> {
        return columns.filter { it.value.foreign }
    }

    fun compositeKey(): Map<String, SQLiteColumn> {
        return primaryKey() //todo watch it, dont know when it will backfire, what if we have a non pk based columns as part of CK?
    }

    fun bindPreparedStatement(values: Array<out String>): List<StatementColumnBinder<*>> {
        val columns = compositeKey().values
        val list = arrayListOf<StatementColumnBinder<*>>()
        columns.mapIndexed { index, column ->
            column.bind(values[index])
        }
        return list
    }

    fun bindCompositeKeyForResultSet(columnValues: Array<out String>): SQLiteWhereClauseBinding {
        val columnNames = compositeKey().keys.map { it }
        return SQLiteWhereClauseBinding(columnNames, columnValues.toList())
    }

    fun bind(columnName: String, value: Any?): StatementColumnBinder<*> {
        val column = columns[columnName]
        return column?.let {
            column.bind(value)
        } ?: kotlin.run {
            throw IllegalAccessException("$columnName not found $name SQLiteTable")
        }
    }

    override fun validate() {
        super.validate()
        if (name.isEmpty()) {
            throw ValidateException(this, "SQLiteTable name should not be empty")
        }
        if (columns.isEmpty()) {
            throw ValidateException(this, "SQLiteTable $name columns should not be empty")
        }
        if (primaryKey().isNull() || primaryKey().isEmpty()) {
            throw ValidateException(this, "SQLiteTable $name must have at least one primary column")
        }
        columns.forEach { it.value.validate() }
    }
}

