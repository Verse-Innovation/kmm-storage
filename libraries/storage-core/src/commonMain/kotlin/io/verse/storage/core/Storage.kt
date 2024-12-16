package io.verse.storage.core

import io.tagd.langx.Callback
import io.tagd.arch.scopable.library.AbstractLibrary
import io.tagd.arch.scopable.library.Library
import io.tagd.di.Scope
import io.tagd.di.bind
import io.verse.storage.core.sql.SQLiteConfig
import io.verse.storage.core.sql.SQLiteDatabase
import io.verse.storage.core.sql.SQLiteOpenHelper

class Storage private constructor(name: String, outerScope: Scope) :
    AbstractLibrary(name, outerScope) {

    lateinit var migrator: Migrator
        private set

    lateinit var dataObjectFileAccessor: DataObjectFileAccessor
        private set

    lateinit var byteFileAccessor: ByteFileAccessor
        private set

    fun openDatabase(config: SQLiteConfig, onOpen: Callback<SQLiteDatabase?>? = null) {
        val sqLiteOpenHelper = SQLiteOpenHelper()
        sqLiteOpenHelper.openWith(config, onOpen)
    }

    class Builder : Library.Builder<Storage>() {

        private lateinit var dataObjectFileAccessor: DataObjectFileAccessor
        private lateinit var byteFileAccessor: ByteFileAccessor
        private var migrator: Migrator? = null

        override fun name(name: String?): Builder {
            this.name = name
            return this
        }

        override fun scope(outer: Scope?): Builder {
            super.scope(outer)
            return this
        }

        fun migrator(migrator: Migrator): Builder {
            this.migrator = migrator
            return this
        }

        fun dataObjectFileAccessor(accessor: DataObjectFileAccessor): Builder {
            this.dataObjectFileAccessor = accessor
            return this
        }

        fun bytesFileAccessor(accessor: ByteFileAccessor): Builder {
            this.byteFileAccessor = accessor
            return this
        }

        override fun buildLibrary(): Storage {
            return Storage(name ?: "${outerScope.name}/$NAME", outerScope).also { storage ->
                storage.dataObjectFileAccessor = dataObjectFileAccessor
                storage.byteFileAccessor = byteFileAccessor
                storage.migrator = migrator ?: Migrator()

                outerScope.bind<Library, Storage>(instance = storage)
            }
        }

        companion object {
            const val NAME = "storage"
        }
    }
}