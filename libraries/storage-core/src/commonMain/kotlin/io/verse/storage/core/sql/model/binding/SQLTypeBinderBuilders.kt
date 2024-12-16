package io.verse.storage.core.sql.model.binding

import io.verse.storage.core.sql.BlobColumnBinder
import io.verse.storage.core.sql.DoubleColumnBinder
import io.verse.storage.core.sql.LongColumnBinder
import io.verse.storage.core.sql.StatementColumnBinder
import io.verse.storage.core.sql.StringColumnBinder
import io.verse.storage.core.sql.model.SQLiteDataTypeHelper
import kotlin.jvm.JvmStatic

fun interface SQLDataTypeBinderBuilder {

    fun build(name: String, data: Any?): StatementColumnBinder<*>
}

object SQLTypeBinderBuilders {

    @JvmStatic
    fun longColumnBinderBuilderMap(): Map<String, SQLDataTypeBinderBuilder> {
        val longColumnBinderBuilder = longColumnBinderBuilder()
        return mapOf(
            SQLiteDataTypeHelper.SQL_TYPE_INT to longColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_INTEGER to longColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_TINYINT to longColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_SMALLINT to longColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_MEDIUMINT to longColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_BIGINT to longColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_UNSIGNED_BIG_INT to longColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_INT2 to longColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_INT8 to longColumnBinderBuilder,
        )
    }

    private fun longColumnBinderBuilder(): SQLDataTypeBinderBuilder {
        return SQLDataTypeBinderBuilder { name, data ->
            val longData = if (data is Long) data else (data as? Number)?.toLong()
            LongColumnBinder(name, longData ?: 0L)
        }
    }

    @JvmStatic
    fun doubleColumnBinderBuilderMap(): Map<String, SQLDataTypeBinderBuilder> {
        val doubleColumnBinderBuilder = doubleColumnBinderBuilder()
        return mapOf(
            SQLiteDataTypeHelper.SQL_TYPE_DOUBLE to doubleColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_REAL to doubleColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_DOUBLE_PRECISION to doubleColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_FLOAT to doubleColumnBinderBuilder,
        )
    }

    private fun doubleColumnBinderBuilder(): SQLDataTypeBinderBuilder {
        return SQLDataTypeBinderBuilder { name, data ->
            DoubleColumnBinder(name, (data as? Double) ?: 0.0)
        }
    }

    @JvmStatic
    fun blobColumnBinderBuilderMap(): Map<String, SQLDataTypeBinderBuilder> {
        val blobColumnBinderBuilder = blobColumnBinderBuilder()
        return mapOf(SQLiteDataTypeHelper.SQL_TYPE_BLOB to blobColumnBinderBuilder)
    }

    private fun blobColumnBinderBuilder(): SQLDataTypeBinderBuilder {
        return SQLDataTypeBinderBuilder { name, data ->
            BlobColumnBinder(name, (data as? ByteArray) ?: ByteArray(0))
        }
    }

    @JvmStatic
    fun stringColumnBinderBuilderMap(): Map<String, SQLDataTypeBinderBuilder> {
        val stringColumnBinderBuilder = stringColumnBinderBuilder()
        return mapOf(
            SQLiteDataTypeHelper.SQL_TYPE_TEXT to stringColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_CHARACTER to stringColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_VARCHAR to stringColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_VARYING_CHARACTER to stringColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_NCHAR to stringColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_NATIVE_CHAR to stringColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_NVARCHAR to stringColumnBinderBuilder,
            SQLiteDataTypeHelper.SQL_TYPE_CLOB to stringColumnBinderBuilder,
        )
    }

    private fun stringColumnBinderBuilder(): SQLDataTypeBinderBuilder {
        return SQLDataTypeBinderBuilder { name, value ->
            StringColumnBinder(name, (value as? String))
        }
    }
}