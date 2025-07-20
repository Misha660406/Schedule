package com.example.schedule.shared.group.domain.usecase

import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.repository.SelectedGroupRepository

class GetSelectedGroupListUseCase(private val repository: SelectedGroupRepository) {

    suspend operator fun invoke(): List<Group> =
        repository.getSelectedGroupList()
}