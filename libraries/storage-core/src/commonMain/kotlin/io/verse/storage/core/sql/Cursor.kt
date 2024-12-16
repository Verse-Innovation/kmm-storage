@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

expect interface Cursor {

    fun getCount(): Int

    fun moveToNext(): Boolean

    fun moveToLast(): Boolean

    fun moveToFirst(): Boolean

    fun moveToPrevious(): Boolean

    fun getColumnIndex(columnName: String?): Int

    fun getColumnIndexOrThrow(columnName: String?): Int

    fun getColumnName(columnIndex: Int): String?

    fun getString(columnIndex: Int): String

    fun getShort(columnIndex: Int): Short

    fun getInt(columnIndex: Int): Int

    fun getLong(columnIndex: Int): Long

    fun getFloat(columnIndex: Int): Float

    fun getDouble(columnIndex: Int): Double

    fun getBlob(columnIndex: Int): ByteArray?

    fun close()
}

//@OptIn(ExperimentalStdlibApi::class)
//interface Cursor : AutoCloseable {
//
//    /**
//     * Returns the numbers of rows in the cursor.
//     *
//     * @return the number of rows in the cursor.
//     */
//    val count: Int
//
//    /**
//     * Returns the current position of the cursor in the row set.
//     * The value is zero-based. When the row set is first returned the cursor
//     * will be at position -1, which is before the first row. After the
//     * last row is returned another call to next() will leave the cursor past
//     * the last entry, at a position of count().
//     *
//     * @return the current cursor position.
//     */
//    val position: Int
//
//    /**
//     * Move the cursor by a relative amount, forward or backward, from the
//     * current position. Positive offsets move forwards, negative offsets move
//     * backwards. If the final position is outside of the bounds of the result
//     * set then the resultant position will be pinned to -1 or count() depending
//     * on whether the value is off the front or end of the set, respectively.
//     *
//     *
//     * This method will return true if the requested destination was
//     * reachable, otherwise, it returns false. For example, if the cursor is at
//     * currently on the second entry in the result set and move(-5) is called,
//     * the position will be pinned at -1, and false will be returned.
//     *
//     * @param offset the offset to be applied from the current position.
//     * @return whether the requested move fully succeeded.
//     */
//    fun move(offset: Int): Boolean
//
//    /**
//     * Move the cursor to an absolute position. The valid
//     * range of values is -1 &lt;= position &lt;= count.
//     *
//     *
//     * This method will return true if the request destination was reachable,
//     * otherwise, it returns false.
//     *
//     * @param position the zero-based position to move to.
//     * @return whether the requested move fully succeeded.
//     */
//    fun moveToPosition(position: Int): Boolean
//
//    /**
//     * Move the cursor to the first row.
//     *
//     *
//     * This method will return false if the cursor is empty.
//     *
//     * @return whether the move succeeded.
//     */
//    fun moveToFirst(): Boolean
//
//    /**
//     * Move the cursor to the last row.
//     *
//     *
//     * This method will return false if the cursor is empty.
//     *
//     * @return whether the move succeeded.
//     */
//    fun moveToLast(): Boolean
//
//    /**
//     * Move the cursor to the next row.
//     *
//     *
//     * This method will return false if the cursor is already past the
//     * last entry in the result set.
//     *
//     * @return whether the move succeeded.
//     */
//    fun moveToNext(): Boolean
//
//    /**
//     * Move the cursor to the previous row.
//     *
//     *
//     * This method will return false if the cursor is already before the
//     * first entry in the result set.
//     *
//     * @return whether the move succeeded.
//     */
//    fun moveToPrevious(): Boolean
//
//    /**
//     * Returns whether the cursor is pointing to the first row.
//     *
//     * @return whether the cursor is pointing at the first entry.
//     */
//    val isFirst: Boolean
//
//    /**
//     * Returns whether the cursor is pointing to the last row.
//     *
//     * @return whether the cursor is pointing at the last entry.
//     */
//    val isLast: Boolean
//
//    /**
//     * Returns whether the cursor is pointing to the position before the first
//     * row.
//     *
//     * @return whether the cursor is before the first result.
//     */
//    val isBeforeFirst: Boolean
//
//    /**
//     * Returns whether the cursor is pointing to the position after the last
//     * row.
//     *
//     * @return whether the cursor is after the last result.
//     */
//    val isAfterLast: Boolean
//
//    /**
//     * Returns the zero-based index for the given column name, or -1 if the column doesn't exist.
//     * If you expect the column to exist use [.getColumnIndexOrThrow] instead, which
//     * will make the error more clear.
//     *
//     * @param columnName the name of the target column.
//     * @return the zero-based column index for the given column name, or -1 if
//     * the column name does not exist.
//     * @see .getColumnIndexOrThrow
//     */
//    fun getColumnIndex(columnName: String?): Int
//
//    /**
//     * Returns the zero-based index for the given column name, or throws
//     * [IllegalArgumentException] if the column doesn't exist. If you're not sure if
//     * a column will exist or not use [.getColumnIndex] and check for -1, which
//     * is more efficient than catching the exceptions.
//     *
//     * @param columnName the name of the target column.
//     * @return the zero-based column index for the given column name
//     * @see .getColumnIndex
//     * @throws IllegalArgumentException if the column does not exist
//     */
//    @Throws(IllegalArgumentException::class)
//    fun getColumnIndexOrThrow(columnName: String?): Int
//
//    /**
//     * Returns the column name at the given zero-based column index.
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return the column name for the given column index.
//     */
//    fun getColumnName(columnIndex: Int): String?
//
//    /**
//     * Returns a string array holding the names of all of the columns in the
//     * result set in the order in which they were listed in the result.
//     *
//     * @return the names of the columns returned in this query.
//     */
//    val columnNames: Array<String?>?
//
//    /**
//     * Return total number of columns
//     * @return number of columns
//     */
//    val columnCount: Int
//
//    /**
//     * Returns the value of the requested column as a byte array.
//     *
//     *
//     * The result and whether this method throws an exception when the
//     * column value is null or the column type is not a blob type is
//     * implementation-defined.
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return the value of that column as a byte array.
//     */
//    fun getBlob(columnIndex: Int): ByteArray?
//
//    /**
//     * Returns the value of the requested column as a String.
//     *
//     *
//     * The result and whether this method throws an exception when the
//     * column value is null or the column type is not a string type is
//     * implementation-defined.
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return the value of that column as a String.
//     */
//    fun getString(columnIndex: Int): String?
//
//    /**
//     * Retrieves the requested column text and stores it in the buffer provided.
//     * If the buffer size is not sufficient, a new char buffer will be allocated
//     * and assigned to CharArrayBuffer.data
//     * @param columnIndex the zero-based index of the target column.
//     * if the target column is null, return buffer
//     * @param buffer the buffer to copy the text into.
//     */
//    //unsupported fun copyStringToBuffer(columnIndex: Int, buffer: android.database.CharArrayBuffer?)
//
//    /**
//     * Returns the value of the requested column as a short.
//     *
//     *
//     * The result and whether this method throws an exception when the
//     * column value is null, the column type is not an integral type, or the
//     * integer value is outside the range [`Short.MIN_VALUE`,
//     * `Short.MAX_VALUE`] is implementation-defined.
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return the value of that column as a short.
//     */
//    fun getShort(columnIndex: Int): Short
//
//    /**
//     * Returns the value of the requested column as an int.
//     *
//     *
//     * The result and whether this method throws an exception when the
//     * column value is null, the column type is not an integral type, or the
//     * integer value is outside the range [`Integer.MIN_VALUE`,
//     * `Integer.MAX_VALUE`] is implementation-defined.
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return the value of that column as an int.
//     */
//    fun getInt(columnIndex: Int): Int
//
//    /**
//     * Returns the value of the requested column as a long.
//     *
//     *
//     * The result and whether this method throws an exception when the
//     * column value is null, the column type is not an integral type, or the
//     * integer value is outside the range [`Long.MIN_VALUE`,
//     * `Long.MAX_VALUE`] is implementation-defined.
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return the value of that column as a long.
//     */
//    fun getLong(columnIndex: Int): Long
//
//    /**
//     * Returns the value of the requested column as a float.
//     *
//     *
//     * The result and whether this method throws an exception when the
//     * column value is null, the column type is not a floating-point type, or the
//     * floating-point value is not representable as a `float` value is
//     * implementation-defined.
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return the value of that column as a float.
//     */
//    fun getFloat(columnIndex: Int): Float
//
//    /**
//     * Returns the value of the requested column as a double.
//     *
//     *
//     * The result and whether this method throws an exception when the
//     * column value is null, the column type is not a floating-point type, or the
//     * floating-point value is not representable as a `double` value is
//     * implementation-defined.
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return the value of that column as a double.
//     */
//    fun getDouble(columnIndex: Int): Double
//
//    /**
//     * Returns data type of the given column's value.
//     * The preferred type of the column is returned but the data may be converted to other types
//     * as documented in the get-type methods such as [.getInt], [.getFloat]
//     * etc.
//     *
//     *
//     * Returned column types are
//     *
//     *  * [.FIELD_TYPE_NULL]
//     *  * [.FIELD_TYPE_INTEGER]
//     *  * [.FIELD_TYPE_FLOAT]
//     *  * [.FIELD_TYPE_STRING]
//     *  * [.FIELD_TYPE_BLOB]
//     *
//     *
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return column value type
//     */
//    fun getType(columnIndex: Int): Int
//
//    /**
//     * Returns `true` if the value in the indicated column is null.
//     *
//     * @param columnIndex the zero-based index of the target column.
//     * @return whether the column value is null.
//     */
//    fun isNull(columnIndex: Int): Boolean
//
//    /**
//     * Deactivates the Cursor, making all calls on it fail until [.requery] is called.
//     * Inactive Cursors use fewer resources than active Cursors.
//     * Calling [.requery] will make the cursor active again.
//     */
//    @Deprecated("Since {@link #requery()} is deprecated, so too is this.")
//    fun deactivate()
//
//    /**
//     * Performs the query that created the cursor again, refreshing its
//     * contents. This may be done at any time, including after a call to [ ][.deactivate].
//     *
//     * Since this method could execute a query on the database and potentially take
//     * a while, it could cause ANR if it is called on Main (UI) thread.
//     * A warning is printed if this method is being executed on Main thread.
//     *
//     * @return true if the requery succeeded, false if not, in which case the
//     * cursor becomes invalid.
//     */
//    @Deprecated(
//        """Don't use this. Just request a new cursor, so you can do this
//      asynchronously and update your list view once the new cursor comes back."""
//    )
//    fun requery(): Boolean
//
//    /**
//     * Closes the Cursor, releasing all of its resources and making it completely invalid.
//     * Unlike [.deactivate] a call to [.requery] will not make the Cursor valid
//     * again.
//     */
//    override fun close()
//
//    /**
//     * return true if the cursor is closed
//     * @return true if the cursor is closed.
//     */
//    val isClosed: Boolean
//
//    /**
//     * Register an observer that is called when changes happen to the content backing this cursor.
//     * Typically the data set won't change until [.requery] is called.
//     *
//     * @param observer the object that gets notified when the content backing the cursor changes.
//     * @see .unregisterContentObserver
//     */
//    //unsupported fun registerContentObserver(observer: ContentObserver?)
//
//    /**
//     * Unregister an observer that has previously been registered with this
//     * cursor via [.registerContentObserver].
//     *
//     * @param observer the object to unregister.
//     * @see .registerContentObserver
//     */
//    //unsupported fun unregisterContentObserver(observer: ContentObserver?)
//
//    /**
//     * Register an observer that is called when changes happen to the contents
//     * of the this cursors data set, for example, when the data set is changed via
//     * [.requery], [.deactivate], or [.close].
//     *
//     * @param observer the object that gets notified when the cursors data set changes.
//     * @see .unregisterDataSetObserver
//     */
//    //unsupported fun registerDataSetObserver(observer: DataSetObserver?)
//
//    /**
//     * Unregister an observer that has previously been registered with this
//     * cursor via [.registerContentObserver].
//     *
//     * @param observer the object to unregister.
//     * @see .registerDataSetObserver
//     */
//    //unsupported fun unregisterDataSetObserver(observer: DataSetObserver?)
//
//    /**
//     * Register to watch a content URI for changes. This can be the URI of a specific data row (for
//     * example, "content://my_provider_type/23"), or a a generic URI for a content type.
//     *
//     * @param cr The content resolver from the caller's context. The listener attached to
//     * this resolver will be notified.
//     * @param uri The content URI to watch.
//     */
//    //unsupported fun setNotificationUri(cr: ContentResolver?, uri: android.net.Uri?)
//
//    /**
//     * Return the URI at which notifications of changes in this Cursor's data
//     * will be delivered, as previously set by [.setNotificationUri].
//     * @return Returns a URI that can be used with
//     * [ ContentResolver.registerContentObserver][ContentResolver.registerContentObserver] to find out about changes to this Cursor's
//     * data.  May be null if no notification URI has been set.
//     */
//    //unsupported val notificationUri: android.net.Uri?
//
//    /**
//     * onMove() will only be called across processes if this method returns true.
//     * @return whether all cursor movement should result in a call to onMove().
//     */
//    val wantsAllOnMoveCalls: Boolean
//    /**
//     * Returns a bundle of extra values. This is an optional way for cursors to provide out-of-band
//     * metadata to their users. One use of this is for reporting on the progress of network requests
//     * that are required to fetch data for the cursor.
//     *
//     *
//     * These values may only change when requery is called.
//     * @return cursor-defined values, or [Bundle.EMPTY][android.os.Bundle.EMPTY] if there
//     * are no values. Never `null`.
//     */
//    /**
//     * Sets a [Bundle] that will be returned by [.getExtras].
//     *
//     * @param extras [Bundle] to set, or null to set an empty bundle.
//     */
//    //unsupported var extras: android.os.Bundle?
//
//    /**
//     * This is an out-of-band way for the the user of a cursor to communicate with the cursor. The
//     * structure of each bundle is entirely defined by the cursor.
//     *
//     *
//     * One use of this is to tell a cursor that it should retry its network request after it
//     * reported an error.
//     * @param extras extra values, or [Bundle.EMPTY][android.os.Bundle.EMPTY].
//     * Never `null`.
//     * @return extra values, or [Bundle.EMPTY][android.os.Bundle.EMPTY].
//     * Never `null`.
//     */
//    //unsupported fun respond(extras: android.os.Bundle?): android.os.Bundle?
//
//    companion object {
//        /*
//     * Values returned by {@link #getType(int)}.
//     * These should be consistent with the corresponding types defined in CursorWindow.h
//     */
//        /** Value returned by [.getType] if the specified column is null  */
//        const val FIELD_TYPE_NULL = 0
//
//        /** Value returned by [.getType] if the specified  column type is integer  */
//        const val FIELD_TYPE_INTEGER = 1
//
//        /** Value returned by [.getType] if the specified column type is float  */
//        const val FIELD_TYPE_FLOAT = 2
//
//        /** Value returned by [.getType] if the specified column type is string  */
//        const val FIELD_TYPE_STRING = 3
//
//        /** Value returned by [.getType] if the specified column type is blob  */
//        const val FIELD_TYPE_BLOB = 4
//    }
//}