package io.verse.storage.core

import io.tagd.arch.domain.crosscutting.codec.JsonCodec
import io.tagd.arch.test.FakeCallbackVerifier
import io.tagd.arch.test.FakeInjector
import io.tagd.langx.IOException
import io.tagd.langx.IllegalValueException
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataObjectFileAccessorTest {

    private val converter: JsonCodec<*> = mock()
    private val byteFileAccessor: ByteFileAccessor = ByteFileAccessor()
    private val fileAccessor = DataObjectFileAccessor(converter, byteFileAccessor)

    private val fileName = "exampleTest123445.txt"

    private val buildPath = "build"
    private val validPath = "$buildPath/childFolder1/childFolder2/"
    private val invalidPath = "/"

    private val data = FilableDataObject()
    private val jsonPersistableObject: JsonPersistableObject = mock()

    private val successVerifier = FakeCallbackVerifier()
    private val failureVerifier = FakeCallbackVerifier()

    @BeforeTest
    fun setup() {
        FakeInjector.inject()

        successVerifier.reset()
        failureVerifier.reset()

        whenever(converter.toJson<Any>(any())).thenReturn("{}")
        whenever(converter.fromJson("{}", FilableDataObject::class)).thenReturn(data)
        whenever(converter.fromJson("{}", JsonPersistableObject::class))
            .thenReturn(jsonPersistableObject)

        whenever(jsonPersistableObject.objectJson).thenReturn("{}")
        whenever(jsonPersistableObject.objectClass).thenReturn(FilableDataObject::javaClass.name)
        whenever(jsonPersistableObject.objectKClass)
            .thenReturn(FilableDataObject::class)
    }

    @AfterTest
    fun tearDown() {
        fileAccessor.deleteAsync(fileName, validPath)
        fileAccessor.deleteAsync(fileName, invalidPath)
        FakeInjector.release()
    }

    @Test
    fun `given if file doesn't exist then write should create the file and then write`() {
        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)

        fileAccessor.writeAsync(data, fileName, validPath)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(true)

        fileAccessor.readAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(data)
    }

    @Test
    fun `given if file already exists then write should overwrite to file`() {
        val dataAlreadyWritten = FilableDataObject()

        fileAccessor.writeAsync(dataAlreadyWritten, fileName, validPath)

        fileAccessor.writeAsync(data, fileName, validPath)

        fileAccessor.readAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(data)
    }

    @Test
    fun `given if there is an error then write should invoke failure`() {
        fileAccessor.writeAsync(data, fileName, invalidPath, failure = failureVerifier.mockMethod)
        failureVerifier.verifyAll {
            assertTrue(it is Throwable)
        }
    }

    @Test
    fun `given a json parsing error occurred then write should invoke failure`() {
        whenever(converter.toJson<Any>(any())).thenReturn(null)

        fileAccessor.writeAsync(data, fileName, validPath, failure = failureVerifier.mockMethod)
        failureVerifier.verifyAll {
            assertTrue(it is IllegalValueException)
            assertEquals("data parsing failed", it.message)
        }
    }

    @Test
    fun `given a json parsing error occurred then read should invoke failure`() {
        whenever(converter.fromJson("{}", FilableDataObject::class))
            .thenReturn(null)

        fileAccessor.writeAsync(data, fileName, validPath)

        fileAccessor.readAsync(fileName, validPath, failure = failureVerifier.mockMethod)
        failureVerifier.verifyAll {
            assertTrue(it is IllegalValueException)
            assertEquals("data parsing failed", it.message)
        }
    }

    @Test
    fun `read should return data written to the file`() {
        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)

        fileAccessor.writeAsync(data, fileName, validPath)

        fileAccessor.readAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(data)
    }

    @Test
    fun `given if file doesn't exist then read should invoke failure`() {
        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)

        fileAccessor.readAsync(fileName, validPath, failure = failureVerifier.mockMethod)
        failureVerifier.verifyAll {
            assertTrue(it is Throwable)
        }
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
        fileAccessor.writeAsync(data, fileName, validPath)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(true)
    }

    @Test
    fun `given an invalid path then exists should return false`() {
        fileAccessor.writeAsync(data, fileName, invalidPath, failure = failureVerifier.mockMethod)
        failureVerifier.verifyAll {
            assertTrue(it is Throwable)
        }

        fileAccessor.existsAsync(fileName, invalidPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)
    }

    @Test
    fun `given if the path is invalid then delete should invoke success`() {
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
        fileAccessor.writeAsync(data, fileName, validPath)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(true)

        fileAccessor.deleteAsync(fileName, validPath)

        fileAccessor.existsAsync(fileName, validPath, success = successVerifier.mockMethod)
        successVerifier.verifyAll(false)
    }

}