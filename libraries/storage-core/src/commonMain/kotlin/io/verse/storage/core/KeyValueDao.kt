package io.verse.storage.core

open class KeyValueDao(name: String, path: String, accessor: DataObjectFileAccessor?) :
    FilableDao<KeyValueDto>(name, path, accessor) {

    @Suppress("warnings")
    override fun interceptReadResult(t: KeyValueDto?): KeyValueDto {
        val result = t ?: KeyValueDto()
        result.dao = this as FilableDao<FilableDataObject>
        return result
    }
}