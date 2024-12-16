package io.verse.storage.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.tagd.arch.test.FakeCallbackVerifier
import io.tagd.arch.test.FakeInjector
import io.tagd.langx.IOException
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class ByteFileAccessorAndroidTest {

    private val fileAccessor = ByteFileAccessor()

    private val mContext = InstrumentationRegistry.getInstrumentation().context

    private val fileName = "exampleTest123445.txt"
    private val permissionGrantedPath = mContext.filesDir.path
    private val permissionNotGrantedPath = ""

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
        fileAccessor.deleteAsync(fileName, permissionGrantedPath)
        fileAccessor.deleteAsync(fileName, permissionNotGrantedPath)
        FakeInjector.release()
    }

    @Test
    fun given_ifPermissionIsNotGrantedForPath_then_writeShouldInvoke_Failure() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(
            data = bytesToBeWritten,
            fileName = fileName,
            path = permissionNotGrantedPath,
            failure = failureVerifier.mockMethod
        )

        failureVerifier.verifyAll {
            assertTrue(it is Throwable)
        }
    }

    @Test
    fun given_ifPermissionIsGrantedForPath_then_writeShouldInvoke_Success() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(
            data = bytesToBeWritten,
            fileName = fileName,
            path = permissionGrantedPath,
            success = successVerifier.mockMethod
        )
        successVerifier.verifyAll(Unit)
    }

    @Test
    fun given_ifPermissionIsNotGrantedForPath_then_readShouldInvoke_Failure() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(
            data = bytesToBeWritten,
            fileName = fileName,
            path = permissionNotGrantedPath
        )

        fileAccessor.readAsync(
            fileName = fileName,
            path = permissionNotGrantedPath,
            failure = failureVerifier.mockMethod
        )

        failureVerifier.verifyAll {
            assertTrue(it is Throwable)
        }
    }

    @Test
    fun given_ifPermissionIsGrantedForPath_then_readShouldInvoke_Success() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(
            data = bytesToBeWritten,
            fileName = fileName,
            path = permissionGrantedPath
        )

        fileAccessor.readAsync(
            fileName = fileName,
            path = permissionGrantedPath,
            success = successVerifier.mockMethod
        )
        successVerifier.verifyAll {
            assertTrue(it is ByteArray)
            assertTrue(bytesToBeWritten.contentEquals(it))
        }
    }

    @Test
    fun given_ifFileDoesNotExist_then_readShouldInvoke_Failure() {
        fileAccessor.readAsync(
            fileName = fileName,
            path = permissionGrantedPath,
            failure = failureVerifier.mockMethod
        )

        failureVerifier.verifyAll {
            assertTrue(it is Throwable)
        }
    }

    @Test
    fun given_ifPermissionIsNotGrantedForPath_then_existsShouldReturn_False() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(
            data = bytesToBeWritten,
            fileName = fileName,
            path = permissionNotGrantedPath
        )

        fileAccessor.existsAsync(
            fileName = fileName,
            path = permissionNotGrantedPath,
            success = successVerifier.mockMethod
        )
        successVerifier.verifyAll(false)
    }

    @Test
    fun given_ifPermissionIsGrantedForPath_then_existsShouldReturn_True() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(
            data = bytesToBeWritten,
            fileName = fileName,
            path = permissionGrantedPath
        )

        fileAccessor.existsAsync(
            fileName = fileName,
            path = permissionGrantedPath,
            success = successVerifier.mockMethod
        )
        successVerifier.verifyAll(true)
    }

    @Test
    fun given_ifPermissionIsNotGrantedForPath_then_deleteShouldInvoke_Failure() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(
            data = bytesToBeWritten,
            fileName = fileName,
            path = permissionNotGrantedPath
        )

        fileAccessor.deleteAsync(
            fileName = fileName,
            path = permissionNotGrantedPath,
            failure = failureVerifier.mockMethod
        )
        failureVerifier.verifyAll {
            assertTrue(it is IOException)
            assertEquals("io operation failed", (it as? IOException)?.message)
        }
    }

    @Test
    fun given_ifPermissionIsGrantedForPath_then_deleteShouldInvoke_Success() {
        val bytesToBeWritten = byteArrayOf(65, 66, 67)

        fileAccessor.writeAsync(
            data = bytesToBeWritten,
            fileName = fileName,
            path = permissionGrantedPath
        )

        fileAccessor.deleteAsync(
            fileName = fileName,
            path = permissionGrantedPath,
            success = successVerifier.mockMethod
        )
        successVerifier.verifyAll(Unit)
    }

}