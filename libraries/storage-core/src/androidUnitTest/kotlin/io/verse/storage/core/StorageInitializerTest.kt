package io.verse.storage.core

import io.tagd.arch.access.library
import io.tagd.arch.test.FakeScopable
import io.tagd.core.dependencies
import io.tagd.di.Global
import io.tagd.di.Scope
import kotlin.test.Test
import kotlin.test.assertEquals

class StorageInitializerTest {

    private val fakeScopable = FakeScopable()

    private val storageInitializer = StorageInitializer(fakeScopable)

    @Test
    fun `new should add the library to a given scope`() {
        val scopeName = "outer-scope"
        val outerScope = Scope(scopeName)
        Global.addSubScope(outerScope)

        val storage = storageInitializer.new(dependencies = dependencies(
            StorageInitializer.ARG_OUTER_SCOPE to outerScope
        ))

        assertEquals(storage, Global.subScope(scopeName)?.library<Storage>())
    }

    @Test
    fun `given no scope then new should add the library to global scope`() {
        val storage = storageInitializer.new(dependencies())

        assertEquals(storage, library<Storage>())
    }

}