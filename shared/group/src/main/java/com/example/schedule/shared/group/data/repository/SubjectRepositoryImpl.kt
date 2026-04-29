package com.example.schedule.shared.group.data.repository

import com.example.schedule.shared.group.domain.repository.SubjectRepository

class SubjectRepositoryImpl(
    private val api: SubjectApi,
) : SubjectRepository {
    override suspend fun getGroupSubjects(groupName: String): List<String> {
        return try {
            api.getGroupSubjects(groupName)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}