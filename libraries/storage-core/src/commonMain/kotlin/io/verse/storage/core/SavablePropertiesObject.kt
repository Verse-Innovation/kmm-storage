package io.verse.storage.core

import io.tagd.arch.datatype.InMemoryPropertiesObject
import io.tagd.arch.datatype.PropertiesObject
import io.tagd.arch.datatype.Property
import io.tagd.core.ValueProvider
import io.tagd.langx.collection.concurrent.ConcurrentHashMap

open class SavablePropertiesObject : KeyValueDto(), PropertiesObject {

    protected open var inMemoryPropertiesObject: InMemoryPropertiesObject? = null

    override var properties: MutableMap<String, Any?>?
        get() = inMemoryPropertiesObject?.properties
        set(value) {
            inMemoryPropertiesObject?.properties = value
        }

    override fun initialize() {
        super.initialize()
        inMemoryPropertiesObject = InMemoryPropertiesObject()
    }

    override fun <T> set(name: String, value: T): SavablePropertiesObject {
        inMemoryPropertiesObject?.set(name, value)
        return this
    }

    override fun <T> set(vararg pairs: Pair<String, T?>): SavablePropertiesObject {
        inMemoryPropertiesObject?.set(*pairs)
        return this
    }

    override fun <T> set(property: Property<T>): SavablePropertiesObject {
        inMemoryPropertiesObject?.set(property)
        return this
    }

    override fun <T> set(vararg property: Property<T>): SavablePropertiesObject {
        inMemoryPropertiesObject?.set(*property)
        return this
    }

    override fun <T> set(name: String, value: ValueProvider<T>): SavablePropertiesObject {
        inMemoryPropertiesObject?.set(name, value)
        return this
    }

    override fun putAll(propertiesObject: PropertiesObject): SavablePropertiesObject {
        inMemoryPropertiesObject?.putAll(propertiesObject)
        return this
    }

    override fun <T> value(name: String): T? {
        return inMemoryPropertiesObject?.value(name)
    }

    override fun <T> property(name: String): Property<T?>? {
        return inMemoryPropertiesObject?.property(name)
    }

    override fun removeProperty(name: String) {
        inMemoryPropertiesObject?.removeProperty(name)
    }

    override fun copySuper(other: PropertiesObject) {
        inMemoryPropertiesObject?.copySuper(other)
    }

    override fun snapshot(): HashMap<String, Any> {
        return inMemoryPropertiesObject?.snapshot() ?: hashMapOf()
    }

    override fun release() {
        inMemoryPropertiesObject?.release()
        inMemoryPropertiesObject = null
        super.release()
    }
}
