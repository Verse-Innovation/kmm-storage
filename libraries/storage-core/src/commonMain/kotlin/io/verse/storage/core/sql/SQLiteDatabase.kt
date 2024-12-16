@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

expect class SQLiteDatabase {

    fun compileStatement(query: String): SQLiteStatement

    fun delete(table: String, whereClause: String?, whereArgs: Array<String?>?): Int

    fun rawQuery(sql: String?, selectionArgs: Array<String?>?): Cursor

    fun beginTransaction()

    fun endTransaction()

    fun setTransactionSuccessful()

    fun inTransaction(): Boolean
}

