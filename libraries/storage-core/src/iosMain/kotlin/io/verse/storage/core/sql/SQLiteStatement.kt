@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

actual class SQLiteStatement {
    actual fun bindNull(index: Int) {
    }

    actual fun bindString(index: Int, value: String) {
    }

    actual fun bindLong(index: Int, value: Long) {
    }

    actual fun bindDouble(index: Int, value: Double) {
    }

    actual fun bindBlob(index: Int, value: ByteArray) {
    }

    actual fun executeInsert(): Long {
        TODO("Not yet implemented")
    }

    actual fun executeUpdateDelete(): Int {
        TODO("Not yet implemented")
    }

    actual fun clearBindings() {
    }

    actual fun close() {
    }
}