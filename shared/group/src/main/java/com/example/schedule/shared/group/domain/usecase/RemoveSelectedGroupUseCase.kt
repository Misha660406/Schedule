package com.example.schedule.shared.group.domain.usecase

import com.example.schedule.shared.group.domain.repository.SelectedGroupRepository

class RemoveSelectedGroupUseCase(private val repository: SelectedGroupRepository) {

    suspend operator fun invoke(id: Long) =
        repository.remove(id)
}