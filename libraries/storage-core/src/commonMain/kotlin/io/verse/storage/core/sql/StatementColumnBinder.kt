package io.verse.storage.core.sql

sealed class StatementColumnBinder<T>(open val columnName: String, open val value: T?) {

    abstract fun bind(statement: SQLiteStatement, index: Int)
}

class StringColumnBinder(columnName: String, value: String?) :
    StatementColumnBinder<String>(columnName, value) {

    override fun bind(statement: SQLiteStatement, index: Int) {
        if (value == null) {
            statement.bindNull(index)
        } else {
            statement.bindString(index, value)
        }
    }
}

class LongColumnBinder(columnName: String, override val value: Long = 0) :
    StatementColumnBinder<Long>(columnName, value) {

    override fun bind(statement: SQLiteStatement, index: Int) {
        statement.bindLong(index, value)
    }
}

class DoubleColumnBinder(columnName: String, override val value: Double = 0.0) :
    StatementColumnBinder<Double>(columnName, value) {

    override fun bind(statement: SQLiteStatement, index: Int) {
        statement.bindDouble(index, value)
    }
}

class BlobColumnBinder(columnName: String, value: ByteArray) :
    StatementColumnBinder<ByteArray>(columnName, value) {

    override fun bind(statement: SQLiteStatement, index: Int) {
        if (value == null) {
            statement.bindNull(index)
        } else {
            statement.bindBlob(index, value)
        }
    }
}