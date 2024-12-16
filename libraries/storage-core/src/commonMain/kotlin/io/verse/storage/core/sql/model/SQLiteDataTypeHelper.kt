package io.verse.storage.core.sql.model

import kotlin.jvm.JvmStatic

object SQLiteDataTypeHelper {

    const val SQL_TYPE_INT = "INT"
    const val SQL_TYPE_INTEGER = "INTEGER"
    const val SQL_TYPE_TINYINT = "TINYINT"
    const val SQL_TYPE_SMALLINT = "SMALLINT"
    const val SQL_TYPE_MEDIUMINT = "MEDIUMINT"
    const val SQL_TYPE_BIGINT = "BIGINT"
    const val SQL_TYPE_UNSIGNED_BIG_INT = "UNSIGNED BIG INT"
    const val SQL_TYPE_INT2 = "INT2"
    const val SQL_TYPE_INT8 = "INT8"

    const val SQL_TYPE_TEXT = "TEXT"
    const val SQL_TYPE_CHARACTER = "CHARACTER"
    const val SQL_TYPE_VARCHAR = "VARCHAR"
    const val SQL_TYPE_VARYING_CHARACTER = "VARYING CHARACTER"
    const val SQL_TYPE_NCHAR = "NCHAR"
    const val SQL_TYPE_NATIVE_CHAR = "NATIVE_CHAR"
    const val SQL_TYPE_NVARCHAR = "NVARCHAR"
    const val SQL_TYPE_CLOB = "CLOB"

    const val SQL_TYPE_BLOB = "BLOB"

    const val SQL_TYPE_DOUBLE = "DOUBLE"
    const val SQL_TYPE_REAL = "REAL"
    const val SQL_TYPE_DOUBLE_PRECISION = "DOUBLE PRECISION"
    const val SQL_TYPE_FLOAT = "FLOAT"

    // Not completely supported
    private const val SQL_TYPE_NUMERIC = "NUMERIC"
    private const val SQL_TYPE_DECIMAL = "DECIMAL"
    private const val SQL_TYPE_BOOLEAN = "BOOLEAN"
    private const val SQL_TYPE_DATE = "DATE"
    private const val SQL_TYPE_DATETIME = "DATETIME"

    @JvmStatic
    fun String.isTypeInt(): Boolean {
        return this.equals(SQL_TYPE_INT, ignoreCase = true)
                || this.equals(SQL_TYPE_INTEGER, ignoreCase = true)
                || this.equals(SQL_TYPE_TINYINT, ignoreCase = true)
                || this.equals(SQL_TYPE_SMALLINT, ignoreCase = true)
                || this.equals(SQL_TYPE_MEDIUMINT, ignoreCase = true)
                || this.equals(SQL_TYPE_BIGINT, ignoreCase = true)
                || this.equals(SQL_TYPE_UNSIGNED_BIG_INT, ignoreCase = true)
                || this.equals(SQL_TYPE_INT2, ignoreCase = true)
                || this.equals(SQL_TYPE_INT8, ignoreCase = true)
    }

    @JvmStatic
    fun String.isTypeText(): Boolean {
        return this.equals(SQL_TYPE_TEXT, ignoreCase = true)
                || this.equals(SQL_TYPE_CHARACTER, ignoreCase = true)
                || this.equals(SQL_TYPE_VARCHAR, ignoreCase = true)
                || this.equals(SQL_TYPE_VARYING_CHARACTER, ignoreCase = true)
                || this.equals(SQL_TYPE_NCHAR, ignoreCase = true)
                || this.equals(SQL_TYPE_NATIVE_CHAR, ignoreCase = true)
                || this.equals(SQL_TYPE_NVARCHAR, ignoreCase = true)
                || this.equals(SQL_TYPE_CLOB, ignoreCase = true)
    }

    @JvmStatic
    fun String.isTypeBlob(): Boolean {
        return this.equals(SQL_TYPE_BLOB, ignoreCase = true)
    }

    @JvmStatic
    fun String.isTypeDouble(): Boolean {
        return this.equals(SQL_TYPE_DOUBLE, ignoreCase = true)
                || this.equals(SQL_TYPE_REAL, ignoreCase = true)
                || this.equals(SQL_TYPE_DOUBLE_PRECISION, ignoreCase = true)
                || this.equals(SQL_TYPE_FLOAT, ignoreCase = true)
    }

    @JvmStatic
    fun String.isTypeNumeric(): Boolean {
        return this.equals(SQL_TYPE_NUMERIC, ignoreCase = true)
                || this.equals(SQL_TYPE_DECIMAL, ignoreCase = true)
                || this.equals(SQL_TYPE_BOOLEAN, ignoreCase = true)
                || this.equals(SQL_TYPE_DATE, ignoreCase = true)
                || this.equals(SQL_TYPE_DATETIME, ignoreCase = true)
    }

    @JvmStatic
    fun String.isValidType(): Boolean {
        return isTypeInt() || isTypeDouble() || isTypeNumeric() || isTypeText() || isTypeBlob()
    }
}