package io.verse.storage.android.domain.dao.binding

import io.tagd.arch.access.dao
import io.tagd.di.Scope
import io.verse.storage.android.domain.dao.AuthorDao
import io.verse.storage.android.domain.dto.BookDto
import io.verse.storage.android.domain.dao.BookStatsDao
import io.verse.storage.core.sql.Cursor
import io.verse.storage.core.sql.TableBinding
import io.verse.storage.core.sql.model.SQLiteTable
import io.verse.storage.core.sql.model.binding.SQLiteRowBinding

class BookTableBinding(private val scope: Scope) : TableBinding<BookDto> {

    private val bookStatsDao
        get() = scope.dao<BookStatsDao>()!!

    private val authorDao
        get() = scope.dao<AuthorDao>()!!

    override val tableName: String = TABLE_NAME
    override lateinit var table: SQLiteTable

    override fun initWith(table: SQLiteTable) {
        this.table = table
    }

    override fun bindRecord(record: BookDto): SQLiteRowBinding {
        val columnBindings = listOf(
            bindColumn(COLUMN_BOOK_ID, record.bookId),
            bindColumn(COLUMN_BOOK_NAME, record.bookName),
            bindColumn(COLUMN_AUTHOR_ID, record.authorDto.authorId)
        )
        val compositeKeyBinding = listOf(
            bindColumn(COLUMN_BOOK_ID, record.bookId)
        )
        return SQLiteRowBinding(compositeKeyBinding, columnBindings)
    }

    override fun getRecord(cursor: Cursor): BookDto {
        val bookId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID))
        val bookName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_NAME))
        val authorId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_ID))

        return BookDto(
            bookId = bookId,
            bookName = bookName,
            statsDto = bookStatsDao.getSynchronous(arrayOf(bookId))!!,
            authorDto = authorDao.getSynchronous(arrayOf(authorId))!!,
        )
    }

    override fun ckColumnValueSet(record: BookDto): Array<String> {
        return arrayOf(record.bookId)
    }

    companion object {
        const val TABLE_NAME = "Book"
        const val COLUMN_BOOK_ID = "book_id"
        const val COLUMN_BOOK_NAME = "book_name"
        const val COLUMN_AUTHOR_ID = AuthorTableBinding.COLUMN_AUTHOR_ID
    }
}