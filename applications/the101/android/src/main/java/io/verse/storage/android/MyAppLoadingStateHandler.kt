package io.verse.storage.android

import io.tagd.android.app.TagdApplication
import io.tagd.android.app.loadingstate.AppLoadingStateHandler
import io.tagd.android.app.loadingstate.AppLoadingStepDispatcher
import io.tagd.di.Global
import io.verse.storage.android.domain.dao.BookDatabase
import io.verse.storage.core.sql.SQLiteConfig
import io.verse.storage.core.sql.SQLiteOpenHelper
import java.lang.ref.WeakReference

class MyAppLoadingStateHandler(
    app: TagdApplication,
    dispatcher: AppLoadingStepDispatcher<out TagdApplication>
) : AppLoadingStateHandler(dispatcher) {

    private var weakApplication: WeakReference<out TagdApplication>? = WeakReference(app)

    private val application
        get() = weakApplication?.get()

    override fun onRegisterStep() {
        super.onRegisterStep()
        register(STEP_INIT_SQLITE, this::setupSqlite)
    }

    private fun setupSqlite() {
        val config = SQLiteConfig(
            context = application!!,
            databaseName = BookDatabase.NAME,
            createSchemaPaths = arrayListOf("create_schema.sql"),
            deleteSchemaPaths = arrayListOf("delete_schema.sql")
        )
        val sqLiteOpenHelper = SQLiteOpenHelper()
        sqLiteOpenHelper.openWith(config) {
            BookDatabase.initDaos(Global)

            onComplete(STEP_INIT_SQLITE)

            println("opened sqlite")
        }
    }


    companion object {
        const val STEP_INIT_SQLITE = "init_sqlite_context"
    }
}