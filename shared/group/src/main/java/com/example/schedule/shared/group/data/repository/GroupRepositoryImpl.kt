package com.example.schedule.shared.group.data.repository

import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.repository.GroupRepository

class GroupRepositoryImpl : GroupRepository {

    override suspend fun getAll(): List<Group> =
        listOf(
            Group(id = 100L, name = "Группа 100"),
            Group(id = 200L, name = "Группа 200"),
            Group(id = 300L, name = "Группа 300")
        )
}