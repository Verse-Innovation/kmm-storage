package io.verse.storage.core.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase

open class PlatformSQLiteOpenHelper(
    open val host: SQLiteOpenHelper,
    context: Context?,
    dbName: String?,
    version: Int
) : android.database.sqlite.SQLiteOpenHelper(context, dbName, null, version) {

    override fun onCreate(db: SQLiteDatabase) {
        host.onCreate(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        host.onUpgrade(db, oldVersion, newVersion)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        host.onOpen(db)
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        host.onConfigure(db)
    }

    companion object {
        const val TAG = "PlatformSQLiteOpenHelper"
    }
}