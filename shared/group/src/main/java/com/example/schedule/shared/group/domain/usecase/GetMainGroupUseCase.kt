package com.example.schedule.shared.group.domain.usecase

import com.example.schedule.shared.group.domain.repository.MainGroupRepository

class GetMainGroupUseCase(private val repository: MainGroupRepository) {

    suspend operator fun invoke(): String = repository.getMainGroup()
}