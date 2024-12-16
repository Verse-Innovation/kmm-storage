@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core

import io.tagd.android.crosscutting.codec.GsonJsonCodec
import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.arch.scopable.library.Library
import io.tagd.core.Dependencies
import io.tagd.di.Scope
import io.tagd.di.bindLazy
import io.tagd.langx.Callback

actual open class StorageInitializer<S : Scopable> actual constructor(
    within: S,
    private val name: String?,
    private val migrator: Migrator?
) : AbstractWithinScopableInitializer<S, Storage>(within) {

    override fun initialize(callback: Callback<Unit>) {
        outerScope.bindLazy<Library, Storage> {
            new(newDependencies() + arrayOf(
                ARG_NAME to name,
                ARG_MIGRATOR to migrator
            ))
        }
        super.initialize(callback)
    }

    override fun new(dependencies: Dependencies): Storage {
        val outerScope = dependencies.get<Scope>(ARG_OUTER_SCOPE)!!
        val name = dependencies.get<String?>(ARG_NAME)
        val migrator = dependencies.get<Migrator?>(ARG_MIGRATOR)

        val bytesFileAccessor = newByteFileAccessor()

        return Storage.Builder()
            .name(name)
            .scope(outerScope)
            .dataObjectFileAccessor(newDataObjectFileAccessor(bytesFileAccessor))
            .bytesFileAccessor(bytesFileAccessor)
            .migrator(migrator ?: newMigrator())
            .build()
    }

    protected open fun newMigrator(): Migrator {
        return Migrator()
    }

    protected open fun newByteFileAccessor(): ByteFileAccessor {
        return ByteFileAccessor()
    }

    protected open fun newDataObjectFileAccessor(
        bytesFileAccessor: ByteFileAccessor,
    ): DataObjectFileAccessor {

        return DataObjectFileAccessor(
            jsonConverter = GsonJsonCodec.new(),
            byteFileAccessor = bytesFileAccessor
        )
    }

    companion object {
        const val ARG_OUTER_SCOPE = AbstractWithinScopableInitializer.ARG_OUTER_SCOPE
        const val ARG_NAME = "name"
        const val ARG_MIGRATOR = "migrator"
    }
}