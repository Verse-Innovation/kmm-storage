@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

expect class SQLiteStatement {

    fun bindNull(index: Int)

    fun bindString(index: Int, value: String)

    fun bindLong(index: Int, value: Long)

    fun bindDouble(index: Int, value: Double)

    fun bindBlob(index: Int, value: ByteArray)

    fun executeInsert(): Long

    fun executeUpdateDelete(): Int

    fun clearBindings()

    fun close()
}