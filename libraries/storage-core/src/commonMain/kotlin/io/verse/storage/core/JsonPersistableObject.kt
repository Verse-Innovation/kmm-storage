package io.verse.storage.core

import io.tagd.arch.datatype.DataObject
import io.tagd.langx.datatype.asKClass
import kotlin.reflect.KClass

data class JsonPersistableObject(
    val objectJson: String,
    val objectClass: String
) : DataObject() {

    val objectKClass: KClass<*>?
        get() {
            return try {
                objectClass.asKClass<Any>()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

}