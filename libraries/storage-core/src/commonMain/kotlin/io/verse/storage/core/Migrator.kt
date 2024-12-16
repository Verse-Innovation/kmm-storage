package io.verse.storage.core

import io.tagd.arch.data.dao.DataAccessObject
import io.tagd.arch.domain.crosscutting.async.AsyncContext
import io.tagd.arch.domain.crosscutting.async.cancelAsync
import io.tagd.arch.domain.crosscutting.async.compute
import io.tagd.arch.domain.crosscutting.async.daoIO
import io.tagd.arch.domain.crosscutting.async.diskIO
import io.tagd.langx.Callback
import io.tagd.di.Global
import io.tagd.di.layer
import io.tagd.langx.ref.concurrent.atomic.AtomicInteger
import io.verse.storage.core.sql.SqlDao

open class Migrator : AsyncContext {

    fun migrate(oldVersion: Int, newVersion: Int, doneCallback: Callback<Unit>) {
        Global.locator.layer {
            this.all().migrate(oldVersion, newVersion, doneCallback)
        }
    }

    private fun List<DataAccessObject>.migrate(
        oldVersion: Int,
        newVersion: Int,
        doneCallback: Callback<Unit>
    ) {

        val sqlDaos = filterIsInstance<SqlDao<*>>()
        val filableDaos = filterIsInstance<FilableDao<*>>()
        val zipper = Zipper( 2) {
            compute { doneCallback.invoke(Unit) }
        }

        daoIO {
            sqlDaos.forEach { dao ->
                dao.migrate(oldVersion, newVersion)
            }
            zipper.zip()
        }
        diskIO {
            filableDaos.forEach { dao ->
                dao.migrate(oldVersion, newVersion)
            }
            zipper.zip()
        }
    }

    override fun release() {
        cancelAsync()
    }
}

private class Zipper(
    private val count: Int,
    val callback: Callback<Unit>
) {

    private val counter = AtomicInteger(0)

    fun zip() {
        counter.set(counter.get() + 1)
        if (counter.get() == count) {
            done()
        }
    }

    private fun done() {
        callback.invoke(Unit)
    }
}