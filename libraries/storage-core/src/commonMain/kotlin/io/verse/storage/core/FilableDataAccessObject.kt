package io.verse.storage.core

import io.tagd.arch.data.dao.DataAccessObject
import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.langx.Callback

interface FilableDataAccessObject<T : FilableDataObject> : DataAccessObject, AsyncContext {

    val cachedDataObject: T?

    fun writeAsync(data: T, success: Callback<Unit>? = null, failure: Callback<Throwable>? = null)

    fun write(data: T, success: Callback<Unit>? = null, failure: Callback<Throwable>? = null)

    fun readAsync(success: Callback<T>? = null, failure: Callback<Throwable>? = null)

    fun read(success: Callback<T>? = null, failure: Callback<Throwable>? = null)

    fun deleteAsync(success: Callback<Unit>? = null, failure: Callback<Throwable>? = null)

    fun delete(success: Callback<Unit>?, failure: Callback<Throwable>?)
}