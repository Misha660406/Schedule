package com.example.schedule.shared.group.domain.usecase

import com.example.schedule.shared.group.domain.repository.SelectedGroupRepository

class SelectGroupUseCase(private val repository: SelectedGroupRepository) {

    suspend operator fun invoke(id: Long) =
        repository.select(id)
}