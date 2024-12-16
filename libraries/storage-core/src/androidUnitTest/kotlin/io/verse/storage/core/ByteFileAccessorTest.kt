package io.verse.storage.core

import io.tagd.arch.test.FakeCallbackVerifier
import io.tagd.arch.test.FakeInjector
import io.tagd.langx.IOException
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ByteFileAccessorTest {

    private val fileAccessor = ByteFileAccessor()

    private val fileName = "exampleTest123445.txt"
    private val buildPath = "build"
    private val validPath = "$buildPath/childFolder1/childFolder2/"
    private val invalidPath = "/"

    private val successVerifier = FakeCallbackVerifier()
    private val failureVerifier = FakeCallbackVerifier()

    @BeforeTest
    fun setup() {
        FakeInjector.inject()
        successVerifier.reset()
        failureVerifier.reset()
    }

    @AfterTest
    fun tearDown() {
        fileAccessor.deleteAsync(fileName, validPath)
        fileAccessor.deleteAsync(fileName, invalidPath)
        FakeInjector.release()
    }

    @Test
    fun `given if file doesn't exist then write should create the file and then write`() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)

        fileAccessor.writeAsync(bytesToBeWritten, fileName, validPath)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(true)

        fileAccessor.readAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll {
            assertTrue(it is ByteArray)
            assertTrue(bytesToBeWritten.contentEquals(it))
        }
    }

    @Test
    fun `given if file already exists then write should overwrite to file`() {
        val bytesAlreadyWritten = byteArrayOf(65, 66, 67)
        val bytesToBeWritten = byteArrayOf(68, 69, 70)

        fileAccessor.writeAsync(bytesAlreadyWritten, fileName, validPath)

        fileAccessor.writeAsync(bytesToBeWritten, fileName, validPath)

        fileAccessor.readAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll {
            assertTrue(it is ByteArray)
            assertTrue(bytesToBeWritten.contentEquals(it))
        }
    }

    @Test
    fun `given an invalid path then write should invoke failure`() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(
            bytesToBeWritten, fileName, invalidPath,
            failure = failureVerifier.mockMethod
        )
        failureVerifier.verifyAll {
            assertTrue(it is Throwable)
        }
    }

    @Test
    fun `read should return bytes written to the file`() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)

        fileAccessor.writeAsync(bytesToBeWritten, fileName, validPath)

        fileAccessor.readAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll {
            assertTrue(it is ByteArray)
            assertTrue(bytesToBeWritten.contentEquals(it))
        }

        fileAccessor.deleteAsync(fileName, validPath)
    }

    @Test
    fun `given if the path is invalid then read should invoke failure`() {
        fileAccessor.readAsync(fileName, invalidPath, failure = failureVerifier.mockMethod)
        failureVerifier.verifyAll {
            assertTrue(it is Throwable)
        }
    }

    @Test
    fun `given if the file doesn't exist then read should invoke failure`() {
        fileAccessor.readAsync(fileName, validPath, failure = failureVerifier.mockMethod)
        failureVerifier.verifyAll {
            assertTrue(it is Throwable)
        }
    }

    @Test
    fun `given if file doesn't exist then exists should return false`() {
        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)
    }

    @Test
    fun `given if file exists then exists should return true`() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(bytesToBeWritten, fileName, validPath)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(true)

        fileAccessor.deleteAsync(fileName, validPath)
    }

    @Test
    fun `given an invalid path then exists should return false`() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(bytesToBeWritten, fileName, invalidPath)

        fileAccessor.existsAsync(fileName, invalidPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)
    }

    @Test
    fun `given if the path is invalid then delete should invoke failure`() {
        fileAccessor.deleteAsync(fileName, invalidPath, failure = failureVerifier.mockMethod)
        failureVerifier.verifyAll {
            assertTrue(it is IOException)
            assertEquals("io operation failed", it.message)
        }
    }

    @Test
    fun `given if the file does not exist then delete should invoke failure`() {
        fileAccessor.deleteAsync(fileName, validPath, failure = failureVerifier.mockMethod)
        failureVerifier.verifyAll {
            assertTrue(it is IOException)
            assertEquals("io operation failed", it.message)
        }
    }

    @Test
    fun `delete should delete the file`() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(bytesToBeWritten, fileName, validPath)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(true)

        fileAccessor.deleteAsync(fileName, validPath)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)
    }

}