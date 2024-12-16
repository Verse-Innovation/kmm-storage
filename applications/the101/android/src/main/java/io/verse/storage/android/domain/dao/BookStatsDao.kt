package io.verse.storage.android.domain.dao

import io.tagd.di.Scope
import io.verse.storage.android.domain.dao.binding.BookStatsTableBinding
import io.verse.storage.android.domain.dto.BookStatsDto
import io.verse.storage.core.sql.SqlDao

class BookStatsDao(scope: Scope, databaseName: String) : SqlDao<BookStatsDto>(
    scope,
    databaseName,
    BookStatsTableBinding()
)