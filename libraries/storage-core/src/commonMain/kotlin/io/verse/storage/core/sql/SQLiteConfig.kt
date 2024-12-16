package io.verse.storage.core.sql

import io.tagd.arch.datatype.DataObject
import io.tagd.di.Global
import io.tagd.di.Scope
import io.tagd.langx.Context

data class SQLiteConfig(
    val context: Context,
    val databaseName: String,
    val version: Int = 1,
    val createSchemaPaths: List<String> = listOf(),
    val deleteSchemaPaths: List<String> = listOf(),
    val parser: SQLSchemaFileParser = SQLSchemaFileParser(),
    val openMode: Int = READ_WRITE,
    val scope: Scope = Global
) : DataObject() {

    fun openForReadWrite(): Boolean {
        return openMode == READ_WRITE
    }

    companion object {
        const val READ_WRITE = 0
        const val READ_ONLY = 1
    }
}