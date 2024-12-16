@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.verse.storage.core.sql

import android.os.Build
import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.arch.domain.crosscutting.async.daoIO
import io.tagd.langx.Callback
import io.tagd.arch.infra.InfraService
import io.tagd.di.Layer
import io.tagd.di.key
import io.tagd.di.layer
import io.verse.storage.core.sql.model.SQLiteTable
import io.verse.storage.core.sql.parser.SQLiteTableParser

actual open class SQLiteOpenHelper actual constructor() : InfraService, AsyncContext {

    enum class State {
        INIT, CONFIGURING, CREATING, UPGRADING, DOWNGRADING, OPENING, OPENED, CLOSE, DELETING
    }

    protected var state: State = State.INIT
    protected lateinit var config: SQLiteConfig
    protected lateinit var delegate: android.database.sqlite.SQLiteOpenHelper

    protected var tables: HashMap<String, SQLiteTable>? = null
    private var onOpenCallback: Callback<SQLiteDatabase?>? = null

    actual open fun openWith(config: SQLiteConfig, onOpen: Callback<SQLiteDatabase?>?) {
        this.state = State.OPENING
        this.config = config
        this.onOpenCallback = onOpen

        daoIO {
            openInternal(config)
        }
    }

    private fun openInternal(config: SQLiteConfig) {
        delegate = PlatformSQLiteOpenHelper(
            host = this,
            context = config.context,
            dbName = config.databaseName,
            version = config.version
        )

        if (config.openForReadWrite()) {
            getWritableDatabase()
        } else {
            getReadableDatabase()
        }
    }

    actual open fun setWriteAheadLoggingEnabled(enabled: Boolean) {
        delegate.setWriteAheadLoggingEnabled(enabled)
    }

    actual open fun setLookasideConfig(slotSize: Int, slotCount: Int) {
        assert(slotSize >= 0 && slotCount >= 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            delegate.setLookasideConfig(slotSize, slotCount)
        }
    }

    actual open fun setIdleConnectionTimeout(idleConnectionTimeoutMs: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            delegate.setIdleConnectionTimeout(idleConnectionTimeoutMs)
        }
    }

    actual open fun getWritableDatabase(): SQLiteDatabase? {
        return delegate.writableDatabase
    }

    actual open fun getReadableDatabase(): SQLiteDatabase? {
        return delegate.readableDatabase
    }

    actual open fun close() {
        delegate.close()
        this.state = State.CLOSE
    }

    actual open fun onConfigure(db: SQLiteDatabase?) {
        state = State.CONFIGURING
        ensureTablesInitialized()
    }

    actual open fun onBeforeDelete(db: SQLiteDatabase?) {
        state = State.DELETING
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    actual open fun onCreate(db: SQLiteDatabase?) {
        state = State.CREATING

        createSchema(db)
    }

    actual open fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        state = State.UPGRADING

        upgradeSchema(db, oldVersion, newVersion)
    }

    private fun upgradeSchema(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val deleteStatements = getStatementsFromSchema(config.deleteSchemaPaths)
        executeSqlStatements(db!!, deleteStatements)
        createSchema(db)
    }

    private fun createSchema(db: SQLiteDatabase?) {
        val createStatements = getStatementsFromSchema(config.createSchemaPaths)
        executeSqlStatements(db!!, createStatements)
        initTables(createStatements)
    }

    private fun getStatementsFromSchema(schemaPaths: List<String>): List<String> {
        return config.parser.parseSqlStatements(
            context = config.context,
            schemaFiles = schemaPaths
        )
    }

    private fun executeSqlStatements(db: SQLiteDatabase, statements: List<String>) {
        statements.forEach { statement ->
            db.execSQL(statement)
        }
    }

    private fun initTables(createStatements: List<String>) {
        val parsedTables = SQLiteTableParser().parse(createStatements)
        tables = HashMap(parsedTables)
    }

    private fun initTables() {
        val createStatements = getStatementsFromSchema(config.createSchemaPaths)
        initTables(createStatements)
    }

    private fun ensureTablesInitialized() {
        if (tables.isNullOrEmpty()) {
            initTables()
        }
    }


    actual open fun onDowngrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        state = State.DOWNGRADING
    }

    actual open fun onOpen(db: SQLiteDatabase?) {
        this.state = State.OPENED
        bindSqliteContext(db)

        onOpenCallback?.invoke(db)
    }

    private fun bindSqliteContext(db: SQLiteDatabase?) {
        with(config.scope) {
            layer {
                bindSqliteContext(db)
            }
        }
    }

    private fun Layer<InfraService>.bindSqliteContext(db: SQLiteDatabase?) {
        bind(
            service = key(key = config.databaseName.lowercase()),
            instance = SQLiteContext(
                config = config,
                helper = this@SQLiteOpenHelper,
                database = db!!,
                tables = tables!!,
            )
        )
    }

    override fun release() {
        tables?.clear()
        tables = null
        close()
    }
}