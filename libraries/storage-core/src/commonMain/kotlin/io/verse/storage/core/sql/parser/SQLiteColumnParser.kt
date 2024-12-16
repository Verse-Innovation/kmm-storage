package io.verse.storage.core.sql.parser

import io.tagd.langx.assert
import io.verse.storage.core.sql.model.SQLiteColumn

open class SQLiteColumnParser {

    open fun parseTableColumns(statement: String): LinkedHashMap<String, SQLiteColumn> {
        val columns = linkedMapOf<String, SQLiteColumn>()
        val columnsSingleLine = getColumnsLine(statement)
        val columnLines = columnsSingleLine.split(COLUMN_SEPARATOR)

        for (line in columnLines) {
            if (!isKeyLine(line)) {
                val column = parseColumn(line)
                columns[column.name] = column
            } else {
                updateColumnsForKey(line, columns)
            }
        }
        return columns
    }

    open fun isKeyLine(line: String): Boolean {
        return isPrimaryKeyLine(line) || isForeignKeyLine(line)
    }

    open fun parseColumn(columnLine: String): SQLiteColumn {
        val name = getColumnName(columnLine)
        val sqlType = getColumnSqlType(columnLine, name)
        val nonNullable = isNonNullable(columnLine)

        return SQLiteColumn(name, sqlType,
            primary = false,
            foreign = false,
            nonNullable = nonNullable
        )
    }

    open fun updateColumnsForKey(keyLine: String, columns: LinkedHashMap<String, SQLiteColumn>) {
        val columnsInKey = keyLine.substring(
            keyLine.indexOf(PARENTHESIS_START) + 1,
            keyLine.lastIndexOf(PARENTHESIS_END)
        ).split(KEY_SEPARATOR)

        val primaryKey: Boolean = isPrimaryKeyLine(keyLine)
        val foreignKey: Boolean = isForeignKeyLine(keyLine)

        for (keyColumns in columnsInKey) {
            assert(columns[keyColumns] != null)
            if (primaryKey) {
                columns[keyColumns] = columns[keyColumns]!!.copy(primary = true)
            }
            if (foreignKey) {
                columns[keyColumns] = columns[keyColumns]!!.copy(foreign = true)
            }
        }
    }

    private fun getColumnsLine(statement: String): String {
        val columnLineBeginIndex = statement.indexOf(PARENTHESIS_START) + 1
        val columnLineEndIndex = statement.lastIndexOf(PARENTHESIS_END)
        return statement.substring(columnLineBeginIndex, columnLineEndIndex)
    }

    private fun isPrimaryKeyLine(line: String): Boolean {
        return line.startsWith(PRIMARY_KEY, ignoreCase = true)
    }

    private fun isForeignKeyLine(line: String): Boolean {
        return line.startsWith(FOREIGN_KEY, ignoreCase = true)
    }

    private fun getColumnName(columnLine: String) =
        columnLine.substring(0, columnLine.indexOf(COLUMN_COMPONENTS_SEPARATOR))

    private fun getColumnSqlType(columnLine: String, name: String): String {
        val sqlTypeStartIndex = columnLine.indexOf(name) + name.length
        val sqlTypeEndIndex = columnLine.indexOf(NOT_NULL, ignoreCase = true)
            .takeIf { it != -1 } ?: columnLine.length
        return columnLine.substring(sqlTypeStartIndex, sqlTypeEndIndex).trim()
    }

    private fun isNonNullable(line: String): Boolean {
        return line.contains(NOT_NULL, ignoreCase = true)
    }

    companion object {

        private const val PARENTHESIS_START = '('
        private const val PARENTHESIS_END = ')'

        private const val COLUMN_SEPARATOR = ","
        private const val COLUMN_COMPONENTS_SEPARATOR = " "
        private const val KEY_SEPARATOR = ","

        private const val NOT_NULL = "NOT NULL"
        private const val PRIMARY_KEY = "PRIMARY KEY"
        private const val FOREIGN_KEY = "FOREIGN KEY"
    }
}