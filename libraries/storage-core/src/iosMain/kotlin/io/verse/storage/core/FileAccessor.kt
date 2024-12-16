@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core

import io.tagd.langx.Callback

actual class DataObjectFileAccessor : FileAccessor<FilableDataObject> {

    override fun write(
        data: FilableDataObject,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    override fun read(
        fileName: String,
        path: String,
        success: Callback<FilableDataObject>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    actual fun <T : FilableDataObject> writeDataObjectAsync(
        data: T,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    actual fun <T : FilableDataObject> readDataObjectAsync(
        fileName: String,
        path: String,
        success: Callback<T>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    actual fun <T : FilableDataObject> writeDataObject(
        data: T,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    actual fun <T : FilableDataObject> readDataObject(
        fileName: String,
        path: String,
        success: Callback<T>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    override fun writeAsync(
        data: FilableDataObject,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?
    ) {
        TODO("Not yet implemented")
    }

    override fun readAsync(
        fileName: String,
        path: String,
        success: Callback<FilableDataObject>?,
        failure: Callback<Throwable>?
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteAsync(
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    override fun delete(
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?
    ) {
        TODO("Not yet implemented")
    }

    override fun existsAsync(
        fileName: String,
        path: String,
        success: Callback<Boolean>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    override fun exists(
        fileName: String,
        path: String,
        success: Callback<Boolean>?,
        failure: Callback<Throwable>?
    ) {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }
}

actual class ByteFileAccessor actual constructor() : FileAccessor<ByteArray> {

    override fun write(
        data: ByteArray,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    override fun read(
        fileName: String,
        path: String,
        success: Callback<ByteArray>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    override fun writeAsync(
        data: ByteArray,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?
    ) {
        TODO("Not yet implemented")
    }

    override fun readAsync(
        fileName: String,
        path: String,
        success: Callback<ByteArray>?,
        failure: Callback<Throwable>?
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteAsync(
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    override fun delete(
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?
    ) {
        TODO("Not yet implemented")
    }

    override fun existsAsync(
        fileName: String,
        path: String,
        success: Callback<Boolean>?,
        failure: Callback<Throwable>?,
    ) {
        TODO("Not yet implemented")
    }

    override fun exists(
        fileName: String,
        path: String,
        success: Callback<Boolean>?,
        failure: Callback<Throwable>?
    ) {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

}