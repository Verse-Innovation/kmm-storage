package io.verse.storage.core

import io.tagd.arch.datatype.DataObject
import io.tagd.core.Initializable
import io.tagd.core.Releasable
import io.tagd.langx.IllegalAccessException
import io.tagd.langx.collection.concurrent.CopyOnWriteArraySet
import kotlin.jvm.Transient

open class FilableDataObject : DataObject(), Initializable, Releasable {

    @Transient
    open var dao: FilableDao<FilableDataObject>? = null

    init {
        initialize()
    }

    override fun initialize() {
        super.initialize()
        if (bindables == null) {
            bindables = CopyOnWriteArraySet()
        }
    }

    open fun commit() {
        dao?.writeAsync(this)
    }

    override fun release() {
        dao = null
    }
}

open class TransactableFilableDataObject : FilableDataObject() {

    @Transient
    protected var inTransaction: Boolean = false
        private set

    fun startTransaction() {
        if (inTransaction) {
            throw IllegalAccessException("transaction already in progress")
        }
        inTransaction = true
    }

    fun commitTransaction() {
        if (!inTransaction) {
            throw IllegalAccessException("there is no active transaction to commit")
        }
        commit()
        inTransaction = false
    }
}