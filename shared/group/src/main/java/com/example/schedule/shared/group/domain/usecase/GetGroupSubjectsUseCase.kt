package com.example.schedule.shared.group.domain.usecase

import com.example.schedule.shared.group.domain.repository.SubjectRepository

class GetGroupSubjectsUseCase(private val repository: SubjectRepository) {
    suspend operator fun invoke(groupName: String): List<String> =
        repository.getGroupSubjects(groupName)
}