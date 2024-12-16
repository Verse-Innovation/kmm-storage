package io.verse.storage.android.domain.dao.binding

import io.verse.storage.android.domain.dto.AuthorDto
import io.verse.storage.core.sql.Cursor
import io.verse.storage.core.sql.TableBinding
import io.verse.storage.core.sql.model.SQLiteTable
import io.verse.storage.core.sql.model.binding.SQLiteRowBinding

class AuthorTableBinding : TableBinding<AuthorDto> {

    override val tableName: String = TABLE_NAME
    override lateinit var table: SQLiteTable

    override fun initWith(table: SQLiteTable) {
        this.table = table
    }

    override fun bindRecord(record: AuthorDto): SQLiteRowBinding {
        val columnBindings = listOf(
            bindColumn(COLUMN_AUTHOR_ID, record.authorId),
            bindColumn(COLUMN_AUTHOR_NAME, record.authorName),
        )
        val compositeKeyBiding = listOf(
            bindColumn(COLUMN_AUTHOR_ID, record.authorId)
        )
        return SQLiteRowBinding(compositeKeyBiding, columnBindings)
    }

    override fun ckColumnValueSet(record: AuthorDto): Array<String> {
        return arrayOf(record.authorId)
    }

    override fun getRecord(cursor: Cursor): AuthorDto {
        return AuthorDto(
            authorId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_ID)),
            authorName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_NAME)),
        )
    }

    companion object {
        const val TABLE_NAME = "Author"
        const val COLUMN_AUTHOR_ID = "author_id"
        const val COLUMN_AUTHOR_NAME = "author_name"
    }
}