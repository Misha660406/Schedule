package com.example.schedule.shared.group.domain.repository

interface SubjectRepository {

    suspend fun getGroupSubjects(groupName: String): List<String>
}