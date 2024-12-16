package io.verse.storage.android.domain.dao

import io.tagd.di.Scope
import io.verse.storage.android.domain.dao.binding.AuthorTableBinding
import io.verse.storage.android.domain.dto.AuthorDto
import io.verse.storage.core.sql.SqlDao

class AuthorDao(scope: Scope, databaseName: String) : SqlDao<AuthorDto>(
    scope,
    databaseName,
    AuthorTableBinding()
)