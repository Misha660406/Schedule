package com.example.schedule.shared.group.data.repository

import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.repository.GroupRepository
import com.example.schedule.shared.group.domain.repository.SelectedGroupRepository

class SelectedGroupRepositoryImpl(
    private val groupRepository: GroupRepository,
) : SelectedGroupRepository {

    private val selectedGroups = mutableSetOf<Group>()
    private var isInitialized = false

    override suspend fun getSelectedGroupList(): List<Group> {
        if (!isInitialized) {
            val allGroups = groupRepository.getAll()
            selectedGroups.addAll(allGroups)
            isInitialized = true
        }
        return selectedGroups.toList()
    }

    override suspend fun remove(id: Long) {
        selectedGroups.removeIf { it.id == id }
    }

    override suspend fun select(id: Long) {
        val allGroups = groupRepository.getAll()
        allGroups.find { it.id == id }?.let { selectedGroups.add(it) }
    }
}