@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core

import io.tagd.arch.scopable.AbstractWithinScopableInitializer
import io.tagd.arch.scopable.Scopable

expect open class StorageInitializer<S : Scopable>(
    within: S,
    name: String? = null,
    migrator: Migrator? = null
) : AbstractWithinScopableInitializer<S, Storage>