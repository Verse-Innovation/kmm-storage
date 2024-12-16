@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable
import io.tagd.core.Dependencies

actual open class StorageInitializer<S : Scopable> actual constructor(
    within: S,
    name: String?,
    migrator: Migrator?
) : AbstractWithinScopableInitializer<S, Storage>(within) {

    override fun new(dependencies: Dependencies): Storage {
        TODO("Not yet implemented")
    }
}