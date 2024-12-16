package io.verse.storage.core.sql.model.binding

class SQLiteWhereClauseBinding(
    columns: List<String>,
    values: List<Any?>?,
) {

    val clause: String
    val args: Array<String?>

    init {
        val columnsStringBuilder = StringBuilder()
        for (column in columns) {
            if (columnsStringBuilder.isNotEmpty()) {
                columnsStringBuilder.append(",")
            }
            columnsStringBuilder.append(column).append("=?")
        }

        clause = columnsStringBuilder.toString()
        args = values?.let {
            Array(values.size) {
                values[it]?.toString()
            }
        } ?: arrayOf()
    }
}