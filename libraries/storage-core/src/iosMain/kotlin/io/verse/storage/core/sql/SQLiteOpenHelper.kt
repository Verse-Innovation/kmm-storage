@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.langx.Callback
import io.tagd.arch.infra.InfraService

actual open class SQLiteOpenHelper actual constructor()  : InfraService, AsyncContext {


    //    enum class State {
//        INIT, OPENING, OPEN, CLOSE
//    }
//
//    private var state: State = State.INIT
//
//    private lateinit var database: SQLiteDatabase
//
//    fun open(config: SQLiteConfig) {
//        database = SQLiteDatabase(config)
//        database.open()
//    }
    actual open fun openWith(config: SQLiteConfig, onOpen: Callback<SQLiteDatabase?>?) {
    }

    /**
     * Enables or disables the use of write-ahead logging for the database.
     *
     * Write-ahead logging cannot be used with read-only databases so the value of
     * this flag is ignored if the database is opened read-only.
     *
     * @param enabled True if write-ahead logging should be enabled, false if it
     * should be disabled.
     *
     * @see SQLiteDatabase.enableWriteAheadLogging
     */
    actual open fun setWriteAheadLoggingEnabled(enabled: Boolean) {
    }

    /**
     * Configures [lookaside memory allocator](https://sqlite.org/malloc.html#lookaside)
     *
     *
     * This method should be called from the constructor of the subclass,
     * before opening the database, since lookaside memory configuration can only be changed
     * when no connection is using it
     *
     *
     * SQLite default settings will be used, if this method isn't called.
     * Use `setLookasideConfig(0,0)` to disable lookaside
     *
     *
     * **Note:** Provided slotSize/slotCount configuration is just a recommendation.
     * The system may choose different values depending on a device, e.g. lookaside allocations
     * can be disabled on low-RAM devices
     *
     * @param slotSize The size in bytes of each lookaside slot.
     * @param slotCount The total number of lookaside memory slots per database connection.
     */
    actual open fun setLookasideConfig(slotSize: Int, slotCount: Int) {
    }

    /**
     * Sets the maximum number of milliseconds that SQLite connection is allowed to be idle
     * before it is closed and removed from the pool.
     *
     *
     * This method should be called from the constructor of the subclass,
     * before opening the database
     *
     * @param idleConnectionTimeoutMs timeout in milliseconds. Use [Long.MAX_VALUE] value
     * to allow unlimited idle connections.
     */
    actual open fun setIdleConnectionTimeout(idleConnectionTimeoutMs: Long) {
    }

    /**
     * Create and/or open a database that will be used for reading and writing. The first time this
     * is called, the database will be opened and onCreate, onUpgrade and/or onOpen will be called.
     * Once opened successfully, the database is cached, so you can call this method every time you
     * need to write to the database.
     * (Make sure to call close when you no longer need the database.) Errors such as bad
     * permissions or a full disk may cause this method to fail, but future attempts may succeed if
     * the problem is fixed.
     *
     * Database upgrade may take a long time, you should not call this method from the application
     * main thread, including from ContentProvider.onCreate().
     *
     * Returns:
     * a read/write database object valid until close is called
     * Throws:
     * SQLiteException – if the database cannot be opened for writing
     */
    actual open fun getWritableDatabase(): SQLiteDatabase? {
        TODO("Not yet implemented")
    }

    /**
     * Create and/or open a database. This will be the same object returned by getWritableDatabase
     * unless some problem, such as a full disk, requires the database to be opened read-only.
     * In that case, a read-only database object will be returned. If the problem is fixed, a
     * future call to getWritableDatabase may succeed, in which case the read-only database object
     * will be closed and the read/write object will be returned in the future.
     * Like getWritableDatabase, this method may take a long time to return, so you should not call
     * it from the application main thread, including from ContentProvider.onCreate().
     *
     * Returns:
     * a database object valid until getWritableDatabase or close is called.
     * Throws:
     * SQLiteException – if the database cannot be opened
     */
    actual open fun getReadableDatabase(): SQLiteDatabase? {
        TODO("Not yet implemented")
    }

    actual open fun close() {
    }

    actual open fun onConfigure(db: SQLiteDatabase?) {
    }

    /**
     * Called before the database is deleted when the version returned by
     * [SQLiteDatabase.getVersion] is lower than the minimum supported version passed (if at
     * all) while creating this helper. After the database is deleted, a fresh database with the
     * given version is created. This will be followed by [.onConfigure] and
     * [.onCreate] being called with a new SQLiteDatabase object
     *
     * @param db the database opened with this helper
     * @see .SQLiteOpenHelper
     * @hide
     */
    actual open fun onBeforeDelete(db: SQLiteDatabase?) {
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    actual open fun onCreate(db: SQLiteDatabase?) {
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     *
     *
     * The SQLite ALTER TABLE documentation can be found
     * [here](http://sqlite.org/lang_altertable.html). If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     *
     *
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     *
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    actual open fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
    }

    /**
     * Called when the database needs to be downgraded. This is strictly similar to
     * [.onUpgrade] method, but is called whenever current version is newer than requested one.
     * However, this method is not abstract, so it is not mandatory for a customer to
     * implement it. If not overridden, default implementation will reject downgrade and
     * throws SQLiteException
     *
     *
     *
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     *
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    actual open fun onDowngrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
    }

    /**
     * Called when the database has been opened.  The implementation
     * should check [SQLiteDatabase.isReadOnly] before updating the
     * database.
     *
     *
     * This method is called after the database connection has been configured
     * and after the database schema has been created, upgraded or downgraded as necessary.
     * If the database connection must be configured in some way before the schema
     * is created, upgraded, or downgraded, do it in [.onConfigure] instead.
     *
     *
     * @param db The database.
     */
    actual open fun onOpen(db: SQLiteDatabase?) {
    }

    override fun release() {
        TODO("Not yet implemented")
    }
}