@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

import io.tagd.langx.Context

expect class SQLSchemaFileParser() {

    fun parseSqlStatements(
        context: Context,
        schemaFiles: List<String>
    ): List<String>
}