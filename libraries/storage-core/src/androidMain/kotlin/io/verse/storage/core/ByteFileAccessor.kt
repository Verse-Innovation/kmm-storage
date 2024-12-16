@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core

import io.tagd.arch.domain.crosscutting.async.cancelComputations
import io.tagd.arch.domain.crosscutting.async.cancelDiskIO
import io.tagd.arch.domain.crosscutting.async.compute
import io.tagd.arch.domain.crosscutting.async.diskIO
import io.tagd.langx.Callback
import io.tagd.langx.IOException
import java.io.File

actual open class ByteFileAccessor : FileAccessor<ByteArray> {

    override fun writeAsync(
        data: ByteArray,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) = diskIO {

        try {
            val file = file(fileName, path)
            if (!file.exists()) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            }

            file.writeBytes(data)

            compute {
                success?.invoke(Unit)
            }
        } catch (e: Exception) {
            e.printStackTrace()

            compute {
                failure?.invoke(e)
            }
        }
    }

    override fun write(
        data: ByteArray,
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?
    ) {

        try {
            val file = file(fileName, path)
            if (!file.exists()) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            }

            file.writeBytes(data)
            success?.invoke(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            failure?.invoke(e)
        }
    }

    override fun readAsync(
        fileName: String,
        path: String,
        success: Callback<ByteArray>?,
        failure: Callback<Throwable>?,
    ) = diskIO {

        try {
            val file = file(fileName, path)
            val readBytes = file.readBytes()

            compute {
                success?.invoke(readBytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()

            compute {
                failure?.invoke(e)
            }
        }
    }

    override fun read(
        fileName: String,
        path: String,
        success: Callback<ByteArray>?,
        failure: Callback<Throwable>?,
    ) {

        try {
            val file = file(fileName, path)
            val readBytes = file.readBytes()
            success?.invoke(readBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            failure?.invoke(e)
        }
    }

    override fun deleteAsync(
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) = diskIO {

        try {
            val file = file(fileName, path)
            val fileDeleted = file.delete()

            compute {
                if (fileDeleted) {
                    success?.invoke(Unit)
                } else {
                    failure?.invoke(ioException())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()

            compute {
                failure?.invoke(e)
            }
        }
    }

    override fun delete(
        fileName: String,
        path: String,
        success: Callback<Unit>?,
        failure: Callback<Throwable>?,
    ) {

        try {
            val file = file(fileName, path)
            val fileDeleted = file.delete()
            if (fileDeleted) {
                success?.invoke(Unit)
            } else {
                failure?.invoke(ioException())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            failure?.invoke(e)
        }
    }

    override fun existsAsync(
        fileName: String,
        path: String,
        success: Callback<Boolean>?,
        failure: Callback<Throwable>?,
    ) = diskIO {

        try {
            val file = file(fileName, path)
            val fileExists = file.exists()

            compute {
                success?.invoke(fileExists)
            }
        } catch (e: Exception) {
            e.printStackTrace()

            compute {
                failure?.invoke(e)
            }
        }
    }

    override fun exists(
        fileName: String,
        path: String,
        success: Callback<Boolean>?,
        failure: Callback<Throwable>?,
    ) {

        try {
            val file = file(fileName, path)
            val fileExists = file.exists()
            success?.invoke(fileExists)
        } catch (e: Exception) {
            e.printStackTrace()
            failure?.invoke(e)
        }
    }

    private fun file(fileName: String, path: String): File {
        return File(getFilePath(fileName, path))
    }

    private fun getFilePath(fileName: String, path: String): String {
        return if (path.isBlank()) {
            fileName
        } else {
            "$path/$fileName"
        }
    }

    private fun ioException() = IOException("io operation failed")

    override fun release() {
        cancelComputations()
        cancelDiskIO()
    }
}