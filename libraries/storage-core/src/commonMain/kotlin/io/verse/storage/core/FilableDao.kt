package io.verse.storage.core

import io.tagd.arch.data.dao.AbstractDao
import io.tagd.arch.domain.crosscutting.async.compute
import io.tagd.langx.Callback
import io.tagd.langx.IllegalValueException

open class FilableDao<T : FilableDataObject>(
    protected open val name: String,
    protected open val path: String,
    private var accessor: DataObjectFileAccessor? = null
) : AbstractDao(), FilableDataAccessObject<T> {

    final override var cachedDataObject: T? = null
        protected set

    override fun writeAsync(data: T, success: Callback<Unit>?, failure: Callback<Throwable>?) {
        compute {
            cachedDataObject = data
            accessor?.writeDataObjectAsync(
                data = data,
                fileName = name,
                path = path,
                success = success,
                failure = failure
            )
        }
    }

    override fun write(data: T, success: Callback<Unit>?, failure: Callback<Throwable>?) {
        cachedDataObject = data
        accessor?.writeDataObject(
            data = data,
            fileName = name,
            path = path,
            success = success,
            failure = failure
        )
    }

    override fun readAsync(success: Callback<T>?, failure: Callback<Throwable>?) {
        compute {
            accessor?.readDataObjectAsync<T>(
                fileName = name,
                path = path,
                success = {
                    interceptReadResult(
                        it,
                        IllegalValueException("read data is null"),
                        success,
                        failure
                    )
                },
                failure = {
                    interceptReadResult(null, it, success, failure)
                }
            )
        }
    }

    override fun read(success: Callback<T>?, failure: Callback<Throwable>?) {
        accessor?.readDataObject<T>(
            fileName = name,
            path = path,
            success = {
                interceptReadResult(
                    it,
                    IllegalValueException("read data is null"),
                    success,
                    failure
                )
            },
            failure = {
                interceptReadResult(null, it, success, failure)
            }
        )
    }

    private fun interceptReadResult(
        result: T? = null,
        error: Throwable? = null,
        success: Callback<T>?,
        failure: Callback<Throwable>?
    ) {

        val interceptedResult = interceptReadResult(result)
        interceptedResult?.initialize()
        cachedDataObject = interceptedResult

        notifyResult(result, success, failure, error)
    }

    protected open fun interceptReadResult(t: T?): T? {
        return t
    }

    private fun notifyResult(
        result: T?,
        success: Callback<T>?,
        failure: Callback<Throwable>?,
        error: Throwable?
    ) {

        result?.let {
            success?.invoke(result)
        } ?: kotlin.run {
            failure?.invoke(error!!)
        }
    }

    override fun deleteAsync(success: Callback<Unit>?, failure: Callback<Throwable>?) {
        compute {
            accessor?.deleteAsync(
                fileName = name,
                path = path,
                success = success,
                failure = failure
            )
        }
    }

    override fun delete(success: Callback<Unit>?, failure: Callback<Throwable>?) {
        accessor?.delete(
            fileName = name,
            path = path,
            success = success,
            failure = failure
        )
    }

    override fun migrate(oldVersion: Int, newVersion: Int) {
        //no-op
    }

    override fun release() {
        accessor = null
    }
}