package io.verse.storage.android.domain.dao.binding

import io.verse.storage.android.domain.dto.BookStatsDto
import io.verse.storage.core.sql.Cursor
import io.verse.storage.core.sql.TableBinding
import io.verse.storage.core.sql.model.SQLiteTable
import io.verse.storage.core.sql.model.binding.SQLiteRowBinding

class BookStatsTableBinding : TableBinding<BookStatsDto> {

    override val tableName: String = TABLE_NAME
    override lateinit var table: SQLiteTable

    override fun initWith(table: SQLiteTable) {
        this.table = table
    }

    override fun bindRecord(record: BookStatsDto): SQLiteRowBinding {
        val columnBindings = listOf(
            bindColumn(COLUMN_BOOK_ID, record.bookId),
            bindColumn(COLUMN_PAGE_COUNT, record.pageCount),
        )
        val compositeKeyBiding = listOf(
            bindColumn(COLUMN_BOOK_ID, record.bookId)
        )
        return SQLiteRowBinding(compositeKeyBiding, columnBindings)
    }

    override fun ckColumnValueSet(record: BookStatsDto): Array<String> {
        return arrayOf(record.bookId)
    }

    override fun getRecord(cursor: Cursor): BookStatsDto {
        return BookStatsDto(
            pageCount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAGE_COUNT)),
        ).also {
            it.setAssociationId(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID))
            )
        }
    }

    companion object {
        const val TABLE_NAME = "BookStats"
        const val COLUMN_BOOK_ID = BookTableBinding.COLUMN_BOOK_ID
        const val COLUMN_PAGE_COUNT = "page_count"
    }
}