@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core

import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.langx.Callback

interface FileAccessor<T> : AsyncContext {

    fun writeAsync(
        data: T,
        fileName: String,
        path: String,
        success: Callback<Unit>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun write(
        data: T,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?
    )

    fun readAsync(
        fileName: String,
        path: String,
        success: Callback<T>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun read(
        fileName: String,
        path: String,
        success: Callback<T>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun deleteAsync(
        fileName: String,
        path: String,
        success: Callback<Unit>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun delete(
        fileName: String,
        path: String,
        success: Callback<Unit>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun existsAsync(
        fileName: String,
        path: String,
        success: Callback<Boolean>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun exists(
        fileName: String,
        path: String,
        success: Callback<Boolean>? = null,
        failure: Callback<Throwable>? = null,
    )
}

expect class DataObjectFileAccessor : FileAccessor<FilableDataObject> {

    fun <T : FilableDataObject> writeDataObjectAsync(
        data: T,
        fileName: String,
        path: String,
        success: Callback<Unit>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun <T : FilableDataObject> writeDataObject(
        data: T,
        fileName: String,
        path: String,
        success: Callback<Unit>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun <T : FilableDataObject> readDataObjectAsync(
        fileName: String,
        path: String,
        success: Callback<T>? = null,
        failure: Callback<Throwable>? = null,
    )

    fun <T : FilableDataObject> readDataObject(
        fileName: String,
        path: String,
        success: Callback<T>? = null,
        failure: Callback<Throwable>? = null,
    )
}

expect class ByteFileAccessor() : FileAccessor<ByteArray>

