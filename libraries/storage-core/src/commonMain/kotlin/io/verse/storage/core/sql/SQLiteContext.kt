package io.verse.storage.core.sql

import io.tagd.arch.infra.InfraService
import io.verse.storage.core.sql.model.SQLiteTable

data class SQLiteContext(
    val config: SQLiteConfig,
    val helper: SQLiteOpenHelper,
    val database: SQLiteDatabase,
    val tables: Map<String, SQLiteTable>
) : InfraService {

    override fun release() {
        //no-opO
    }
}