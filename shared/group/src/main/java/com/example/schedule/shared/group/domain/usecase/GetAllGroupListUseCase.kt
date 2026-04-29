package com.example.schedule.shared.group.domain.usecase

import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.repository.GroupRepository

class GetAllGroupListUseCase(private val repository: GroupRepository) {

    suspend operator fun invoke(): List<Group> =
        repository.getAll()
}