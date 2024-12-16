package io.verse.storage.core.sql.model

import io.tagd.arch.datatype.DataObject
import io.tagd.core.ValidateException
import io.tagd.langx.IllegalValueException
import io.tagd.langx.isNull
import io.verse.storage.core.sql.StatementColumnBinder
import io.verse.storage.core.sql.model.binding.SQLDataTypeBinderBuilder
import io.verse.storage.core.sql.model.binding.SQLTypeBinderBuilders

data class SQLiteColumn(
    val name: String,
    val sqlType: String,
    val primary: Boolean,
    val foreign: Boolean,
    val nonNullable: Boolean,
) : DataObject() {


    fun bind(value: Any?): StatementColumnBinder<*> {
        if (nonNullable && value.isNull()) {
            val msg = "Unable to bind column($name) nullable($nonNullable) with value($value)"
            throw IllegalValueException(msg)
        }
        val binderBuilder = typeToBinderMap[sqlType]

        return binderBuilder?.let {
            binderBuilder.build(name, value)
        } ?: kotlin.run {
            val msg = "Unable to find binder for sqlType($sqlType) in column($name)"
            throw IllegalStateException(msg)
        }
    }

    override fun validate() {
        super.validate()
        if (name.isEmpty()) {
            throw ValidateException(this, "SQLiteColumn name should not be empty")
        }
        if (!isValidSqlType()) {
            throw ValidateException(this, "Invalid sqlType ($sqlType) in SQLiteColumn ($name)")
        }
        if (primary && !nonNullable) {
            throw ValidateException(this, "Primary key ($name) must not be nonNullable")
        }
    }

    private fun isValidSqlType(): Boolean {
        return with(SQLiteDataTypeHelper) { sqlType.isValidType() }
    }

    companion object {
        private val typeToBinderMap: Map<String, SQLDataTypeBinderBuilder> =
            HashMap<String, SQLDataTypeBinderBuilder>().apply {
                putAll(SQLTypeBinderBuilders.longColumnBinderBuilderMap())
                putAll(SQLTypeBinderBuilders.doubleColumnBinderBuilderMap())
                putAll(SQLTypeBinderBuilders.stringColumnBinderBuilderMap())
                putAll(SQLTypeBinderBuilders.blobColumnBinderBuilderMap())
            }
    }
}