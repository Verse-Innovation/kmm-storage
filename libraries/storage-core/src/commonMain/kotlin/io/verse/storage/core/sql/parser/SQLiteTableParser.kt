package io.verse.storage.core.sql.parser

import io.tagd.langx.assert
import io.verse.storage.core.sql.model.SQLiteTable

open class SQLiteTableParser {

    open val columnParser = SQLiteColumnParser()

    open fun parse(statements: List<String>): Map<String, SQLiteTable> {
        val tables = hashMapOf<String, SQLiteTable>()

        statements.forEach { statement ->
            if (isCreateStatement(statement)) {
                val table = parse(statement)
                tables[table.name.lowercase()] = table
            }
        }
        return tables
    }

    private fun isCreateStatement(statement: String): Boolean {
        return statement.startsWith(CREATE_QUERY, ignoreCase = true)
    }

    open fun parse(statement: String): SQLiteTable {
        assert(isCreateStatement(statement))

        val tableName = parseTableName(statement)
        val columns = columnParser.parseTableColumns(statement)

        val table = SQLiteTable(tableName, columns)
        table.validate()
        return table
    }

    open fun parseTableName(statement: String): String {
        val createLine = getCreateLine(statement)

        val words = createLine.trim().split(CREATE_QUERY_SEPARATOR)
        val tableName = words.last()
        return tableName.trim()
    }

    private fun getCreateLine(statement: String): String {
        val createLineStartIndex = 0
        val createLineLastIndex = statement.indexOf(CREATE_STATEMENT_END_CHAR)
        return statement.substring(createLineStartIndex, createLineLastIndex)
    }


    companion object {

        private const val CREATE_STATEMENT_END_CHAR = '('
        private const val CREATE_QUERY = "CREATE"
        private const val CREATE_QUERY_SEPARATOR = " "
    }
}