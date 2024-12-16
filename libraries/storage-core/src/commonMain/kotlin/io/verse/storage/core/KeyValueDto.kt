package io.verse.storage.core

import io.tagd.core.Releasable
import io.tagd.langx.collection.concurrent.ConcurrentHashMap
import io.tagd.langx.datatype.Serializable

open class KeyValueDto : TransactableFilableDataObject(), Releasable {

    protected open var keyValues = ConcurrentHashMap<String, Any>()

    init {
        initialize()
    }

    override fun initialize() {
        super.initialize()
        if (keyValues == null) {
            keyValues = ConcurrentHashMap()
        }
    }

    fun putBoolean(name: String, value: Boolean): KeyValueDto {
        return put(name, value)
    }

    fun putByte(name: String, value: Byte): KeyValueDto {
        return put(name, value)
    }

    fun putShort(name: String, value: Short): KeyValueDto {
        return put(name, value)
    }

    fun putInt(name: String, value: Int): KeyValueDto {
        return put(name, value)
    }

    fun putFloat(name: String, value: Float): KeyValueDto {
        return put(name, value)
    }

    fun putLong(name: String, value: Long): KeyValueDto {
        return put(name, value)
    }

    fun putDouble(name: String, value: Double): KeyValueDto {
        return put(name, value)
    }

    fun putString(name: String, value: String): KeyValueDto {
        return put(name, value)
    }

    fun putSerializable(name: String, value: Serializable): KeyValueDto {
        return put(name, value)
    }

    fun putBooleanArray(name: String, value: BooleanArray): KeyValueDto {
        return put(name, value)
    }

    fun putByteArray(name: String, value: ByteArray): KeyValueDto {
        return put(name, value)
    }

    fun putShortArray(name: String, value: ShortArray): KeyValueDto {
        return put(name, value)
    }

    fun putIntArray(name: String, value: IntArray): KeyValueDto {
        return put(name, value)
    }

    fun putFloatArray(name: String, value: FloatArray): KeyValueDto {
        return put(name, value)
    }

    fun putLongArray(name: String, value: LongArray): KeyValueDto {
        return put(name, value)
    }

    fun putDoubleArray(name: String, value: DoubleArray): KeyValueDto {
        return put(name, value)
    }

    fun putArray(name: String, value: Array<*>): KeyValueDto {
        return put(name, value)
    }

    fun putList(name: String, value: List<*>): KeyValueDto {
        return put(name, value)
    }

    fun putMap(name: String, value: Map<*, *>): KeyValueDto {
        return put(name, value)
    }

    private fun put(name: String, value: Any): KeyValueDto {
        keyValues[name] = value
        if (!inTransaction) commit()
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, default: T?): T? {

        val converted = when (val value = keyValues[key]) {
            is Double -> {
                toValue<T>(default, value)
            }

            is Double? -> {
                toNullableValue<T>(default, value)
            }

            else -> {
                value
            }
        }

        return (converted as T?) ?: default
    }

    private fun <T> toValue(default: T?, value: Double) = when (default) {
        is Byte?, is Byte -> {
            value.toInt().toByte()
        }

        is Short?, is Short -> {
            value.toInt().toShort()
        }

        is Int?, is Int -> {
            value.toInt()
        }

        is Float?, is Float -> {
            value.toFloat()
        }

        else -> {
            value
        }
    }

    private fun <T> toNullableValue(default: T?, value: Double?) = when (default) {
        is Byte?, is Byte -> {
            value?.toInt()?.toByte()
        }

        is Short?, is Short -> {
            value?.toInt()?.toShort()
        }

        is Int?, is Int -> {
            value?.toInt()
        }

        is Float?, is Float -> {
            value?.toFloat()
        }

        else -> {
            value
        }
    }

    fun remove(name: String): KeyValueDto {
        keyValues.remove(name)?.also {
            if (!inTransaction) {
                commit()
            }
        }
        return this
    }

    override fun release() {
        keyValues.clear()
    }
}