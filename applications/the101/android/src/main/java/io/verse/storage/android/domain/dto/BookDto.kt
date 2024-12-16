package io.verse.storage.android.domain.dto

import io.tagd.arch.datatype.DataObject

data class BookDto(
    val bookId: String,
    val bookName: String,
    val statsDto: BookStatsDto,
    val authorDto: AuthorDto,
) : DataObject() {

    init {
        enrichAssociations()
    }

    private fun enrichAssociations() {
        statsDto.setAssociationId(bookId)
    }
}

data class BookStatsDto(val pageCount: Int) : DataObject() {
    lateinit var bookId: String

    fun setAssociationId(associationId: String) {
        bookId = associationId
    }
}

data class AuthorDto(val authorId: String, val authorName: String) : DataObject()