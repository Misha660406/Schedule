package com.example.schedule.shared.group.data.repository

import com.example.schedule.shared.group.data.GroupApi
import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.repository.GroupRepository

class GroupRepositoryImpl(
    private val api: GroupApi,
) : GroupRepository {

    private var cachedGroups: List<Group>? = null

    override suspend fun getAll(): List<Group> {
        cachedGroups?.let { return it }

        return try {
            val response = api.getGroups()
            val groups = response.data.map { dto ->
                Group(id = dto.id, name = dto.name)
            }
            cachedGroups = groups
            groups
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}