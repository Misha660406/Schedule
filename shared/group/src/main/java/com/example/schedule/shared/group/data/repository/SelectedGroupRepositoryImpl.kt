package com.example.schedule.shared.group.data.repository

import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.repository.SelectedGroupRepository

class SelectedGroupRepositoryImpl : SelectedGroupRepository {

    private val selectedGroups = mutableSetOf<Group>()

    override suspend fun getSelectedGroupList(): List<Group> =
        selectedGroups.toList()

    override suspend fun remove(id: Long) {
        selectedGroups.removeIf { it.id == id }
    }

    override suspend fun select(id: Long) {
        when (id) {
            100L -> Group(id = 100, name = "Группа 100")
            200L -> Group(id = 200, name = "Группа 200")
            300L -> Group(id = 300, name = "Группа 300")
            else -> null
        }?.let(selectedGroups::add)
    }
}