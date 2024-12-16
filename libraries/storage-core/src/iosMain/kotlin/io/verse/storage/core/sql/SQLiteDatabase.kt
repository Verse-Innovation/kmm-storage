@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

actual class SQLiteDatabase {
    actual fun compileStatement(query: String): SQLiteStatement {
        TODO("Not yet implemented")
    }

    actual fun delete(
        table: String,
        whereClause: String?,
        whereArgs: Array<String?>?
    ): Int {
        TODO("Not yet implemented")
    }

    actual fun rawQuery(
        sql: String?,
        selectionArgs: Array<String?>?
    ): Cursor {
        TODO("Not yet implemented")
    }

    actual fun beginTransaction() {
    }

    actual fun endTransaction() {
    }

    actual fun setTransactionSuccessful() {
    }

    actual fun inTransaction(): Boolean {
        TODO("Not yet implemented")
    }
}