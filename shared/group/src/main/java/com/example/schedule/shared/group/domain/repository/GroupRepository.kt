package com.example.schedule.shared.group.domain.repository

import com.example.schedule.shared.group.domain.entity.Group

interface GroupRepository {

    suspend fun getAll(): List<Group>
}
