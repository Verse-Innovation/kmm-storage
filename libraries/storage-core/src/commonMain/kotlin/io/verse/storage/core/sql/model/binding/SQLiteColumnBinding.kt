package io.verse.storage.core.sql.model.binding

import io.verse.storage.core.sql.StatementColumnBinder

data class SQLiteColumnBinding(
    val columnName: String,
    val binder: StatementColumnBinder<*>,
)