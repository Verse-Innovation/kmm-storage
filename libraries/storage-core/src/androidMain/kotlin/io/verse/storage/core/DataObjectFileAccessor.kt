@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core

import io.tagd.arch.domain.crosscutting.async.cancelComputations
import io.tagd.arch.domain.crosscutting.async.compute
import io.tagd.arch.domain.crosscutting.codec.JsonCodec
import io.tagd.langx.Callback
import io.tagd.langx.IllegalValueException
import java.lang.Exception

actual class DataObjectFileAccessor(
    private val jsonConverter: JsonCodec<*>,
    private val byteFileAccessor: ByteFileAccessor,
) : FileAccessor<FilableDataObject> {

    override fun writeAsync(
        data: FilableDataObject,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) = compute {
        // the intention to have this extra compute is, to ensure that callbacks happen only on
        // compute thread

        try {
            val jsonPersistableObject = toJsonPersistableObject(data, data.javaClass.name)
            val jsonString = jsonConverter.toJson(jsonPersistableObject)
            val bytesToBeWritten = jsonString.encodeToByteArray()

            byteFileAccessor.writeAsync(bytesToBeWritten, fileName, path, success, failure)
        } catch (e: Exception) {
            e.printStackTrace()

            failure?.invoke(parsingException())
        }
    }

    override fun write(
        data: FilableDataObject,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?
    ) {

        try {
            val jsonPersistableObject = toJsonPersistableObject(data, data.javaClass.name)
            val jsonString = jsonConverter.toJson(jsonPersistableObject)
            val bytesToBeWritten = jsonString.encodeToByteArray()

            byteFileAccessor.write(bytesToBeWritten, fileName, path, success, failure)
        } catch (e: Exception) {
            e.printStackTrace()

            failure?.invoke(parsingException())
        }
    }

    actual fun <T : FilableDataObject> writeDataObjectAsync(
        data: T,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) = compute {

        // the intention to have this extra compute is, to ensure that callbacks happen only on
        // compute thread
        writeAsync(data, fileName, path, success, failure)
    }

    actual fun <T : FilableDataObject> writeDataObject(
        data: T,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) {

        write(data, fileName, path, success, failure)
    }

    override fun readAsync(
        fileName: String,
        path: String,
        success: Callback<FilableDataObject>?,
        failure: Callback<Throwable>?,
    ) = compute {

        // the intention to have this extra compute is, to ensure that callbacks happen only on
        // compute thread

        byteFileAccessor.readAsync(
            fileName = fileName,
            path = path,
            success = { bytesRead ->

                val jsonString = bytesRead.decodeToString()
                val jsonPersistableObject = jsonConverter.fromJson(
                    jsonString, JsonPersistableObject::class
                )
                val filableData = jsonPersistableObject.let {
                    val classType = it.objectKClass ?: return@let null
                    jsonConverter.fromJson(it.objectJson, classType)
                }

                (filableData as? FilableDataObject)?.let {
                    success?.invoke(it)
                } ?: kotlin.run {
                    failure?.invoke(parsingException())
                }
            },
            failure = failure
        )
    }

    override fun read(
        fileName: String,
        path: String,
        success: Callback<FilableDataObject>?,
        failure: Callback<Throwable>?
    ) {

        byteFileAccessor.read(
            fileName = fileName,
            path = path,
            success = { bytesRead ->

                val jsonString = bytesRead.decodeToString()
                val jsonPersistableObject = jsonConverter.fromJson(
                    jsonString, JsonPersistableObject::class
                )
                val filableData = jsonPersistableObject.let {
                    val classType = it.objectKClass ?: return@let null
                    jsonConverter.fromJson(it.objectJson, classType)
                }

                (filableData as? FilableDataObject)?.let {
                    success?.invoke(it)
                } ?: kotlin.run {
                    failure?.invoke(parsingException())
                }
            },
            failure = failure
        )
    }

    @Suppress("UNCHECKED_CAST")
    actual fun <T : FilableDataObject> readDataObjectAsync(
        fileName: String,
        path: String,
        success: Callback<T>?,
        failure: Callback<Throwable>?,
    ) = compute {

        // the intention to have this extra compute is, to ensure that callbacks happen only on
        // compute thread
        readAsync(
            fileName = fileName,
            path = path,
            success = { filableDataObject ->
                (filableDataObject as? T)?.let {
                    success?.invoke(it)
                } ?: kotlin.run {
                    failure?.invoke(parsingException())
                }
            },
            failure = failure
        )
    }

    @Suppress("UNCHECKED_CAST")
    actual fun <T : FilableDataObject> readDataObject(
        fileName: String,
        path: String,
        success: Callback<T>?,
        failure: Callback<Throwable>?,
    ) {

        read(
            fileName = fileName,
            path = path,
            success = { filableDataObject ->
                (filableDataObject as? T)?.let {
                    success?.invoke(it)
                } ?: kotlin.run {
                    failure?.invoke(parsingException())
                }
            },
            failure = failure
        )
    }

    override fun deleteAsync(
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) = compute {

        // the intention to have this extra compute is, to ensure that callbacks happen only on
        // compute thread
        byteFileAccessor.deleteAsync(fileName, path, success, failure)
    }

    override fun delete(
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?
    ) {

        byteFileAccessor.delete(fileName, path, success, failure)
    }

    override fun existsAsync(
        fileName: String,
        path: String,
        success: Callback<Boolean>?,
        failure: Callback<Throwable>?,
    ) = compute {

        // the intention to have this extra compute is, to ensure that callbacks happen only on
        // compute thread
        byteFileAccessor.existsAsync(fileName, path, success, failure)
    }

    override fun exists(
        fileName: String,
        path: String,
        success: Callback<Boolean>?,
        failure: Callback<Throwable>?
    ) {

        byteFileAccessor.exists(fileName, path, success, failure)
    }

    private fun <T : Any> toJsonPersistableObject(
        data: T,
        className: String,
    ): JsonPersistableObject {

        val jsonString = jsonConverter.toJson(data) ?: "{}"

        return JsonPersistableObject(
            objectJson = jsonString,
            objectClass = className
        )
    }

    private fun parsingException() = IllegalValueException("data parsing failed")

    override fun release() {
        byteFileAccessor.release()
        cancelComputations()
    }
}