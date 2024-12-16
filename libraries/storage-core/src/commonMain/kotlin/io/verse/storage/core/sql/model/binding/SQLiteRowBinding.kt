package io.verse.storage.core.sql.model.binding

import kotlin.jvm.JvmStatic

data class SQLiteRowBinding(
    var compositeKeyBinding: List<SQLiteColumnBinding>, // todo unused
    val columnBindings: List<SQLiteColumnBinding>,
) {

    val columnCsv: String
    val placeholderCsv: String

    init {
        val columnList = arrayListOf<String>()
        val placeHolderList = arrayListOf<String>()

        columnBindings.forEach { columnBinding ->
            columnList.add(columnBinding.binder.columnName)
            placeHolderList.add("?")
        }

        columnCsv = columnList.joinToString(COLUMN_CSV_SEPARATOR)
        placeholderCsv = placeHolderList.joinToString(COLUMN_CSV_SEPARATOR)
    }

    companion object {

        private const val COLUMN_CSV_SEPARATOR = ", "

        @JvmStatic
        fun placeHoldersCsv(size: Int, placeHolderString: String = "?"): String {
            return if (size > 0) {
                val stringBuilder = StringBuilder()
                for (i in 0 until size - 1) {
                    stringBuilder.append("$placeHolderString, ")
                }
                stringBuilder.append(placeHolderString)
                stringBuilder.toString()
            } else {
                ""
            }
        }
    }
}
