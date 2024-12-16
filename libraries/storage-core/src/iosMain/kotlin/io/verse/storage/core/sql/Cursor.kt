@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

actual interface Cursor {
    actual fun getCount(): Int
    actual fun moveToNext(): Boolean
    actual fun moveToLast(): Boolean
    actual fun moveToFirst(): Boolean
    actual fun moveToPrevious(): Boolean
    actual fun getColumnIndex(columnName: String?): Int
    actual fun getColumnIndexOrThrow(columnName: String?): Int
    actual fun getColumnName(columnIndex: Int): String?
    actual fun getString(columnIndex: Int): String
    actual fun getShort(columnIndex: Int): Short
    actual fun getInt(columnIndex: Int): Int
    actual fun getLong(columnIndex: Int): Long
    actual fun getFloat(columnIndex: Int): Float
    actual fun getDouble(columnIndex: Int): Double
    actual fun getBlob(columnIndex: Int): ByteArray?
    actual fun close()
}