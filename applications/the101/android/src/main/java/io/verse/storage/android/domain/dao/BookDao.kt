package io.verse.storage.android.domain.dao

import io.tagd.arch.access.dao
import io.tagd.di.Global
import io.tagd.di.Scope
import io.verse.storage.android.domain.dao.binding.BookTableBinding
import io.verse.storage.android.domain.dto.BookDto
import io.verse.storage.core.sql.SqlDao

class BookDao(scope: Scope, databaseName: String) : SqlDao<BookDto>(
    scope = scope,
    databaseName = databaseName,
    binding = BookTableBinding(Global)
) {

    private val bookStatsDao
        get() = scope.dao<BookStatsDao>()!!

    private val authorDao
        get() = scope.dao<AuthorDao>()!!

    override fun createSynchronous(row: BookDto): Int {
        bookStatsDao.createSynchronous(row.statsDto)
        authorDao.updateSynchronous(row.authorDto)
        return super.createSynchronous(row)
    }

    override fun deleteCascadeSynchronous(vararg ckColumnValues: String): Int {
        return getSynchronous(ckColumnValues)?.let { bookDto ->
            deleteAuthorsAssociationIfItIsLast(bookDto)
            bookStatsDao.deleteSynchronous(arrayOf(bookDto.bookId))
            super.deleteCascadeSynchronous(*ckColumnValues)
        } ?: -1
    }

    /**
     * //associationCount = getAssociationCount(wc : associationColumnName, args: associationColumnValue)
     */
    private fun deleteAuthorsAssociationIfItIsLast(bookDto: BookDto): Int {
        val authorsCount = getCountSynchronous(
            "${BookTableBinding.COLUMN_AUTHOR_ID}=?",
            arrayOf(bookDto.authorDto.authorId)
        )
        return if (authorsCount == 1) {
            authorDao.deleteSynchronous(bookDto.authorDto)
        } else {
            -1
        }
    }
}
