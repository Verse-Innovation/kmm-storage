package io.verse.storage.android.domain.dao

import io.tagd.arch.data.dao.DataAccessObject
import io.tagd.di.Scope
import io.tagd.di.key
import io.tagd.di.layer

object BookDatabase {

    const val NAME = "sample_database.db"

    fun initDaos(scope: Scope) {
        with(scope) {
            layer<DataAccessObject> {
                bind(key<BookStatsDao>(), BookStatsDao(scope, NAME))
                bind(key<AuthorDao>(), AuthorDao(scope, NAME))
                bind(key<BookDao>(), BookDao(scope, NAME))
            }
        }
    }
}