package io.verse.storage.core.sql

open class SQLiteException : RuntimeException {

    constructor()

    constructor(error: String?) : super(error)

    constructor(error: String?, cause: Throwable?) : super(error, cause)
}