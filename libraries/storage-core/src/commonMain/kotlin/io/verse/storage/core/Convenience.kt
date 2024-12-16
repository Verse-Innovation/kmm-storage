package io.verse.storage.core

import io.tagd.arch.access.infraService
import io.tagd.di.Global
import io.tagd.di.Scope
import io.tagd.di.key
import io.verse.storage.core.sql.SQLiteContext
import io.verse.storage.core.sql.SQLiteDatabase
import io.verse.storage.core.sql.model.SQLiteTable

fun sqliteDatabase(name: String, scope: Scope = Global): SQLiteDatabase? {
    return scope.infraService(key = key<SQLiteContext>(key = name.lowercase()))?.database
}

fun sqliteTable(tableName: String, databaseName: String, scope: Scope = Global): SQLiteTable? {
    return scope.infraService(key = key<SQLiteContext>(key = databaseName.lowercase()))
        ?.tables
        ?.get(tableName.lowercase())
}
