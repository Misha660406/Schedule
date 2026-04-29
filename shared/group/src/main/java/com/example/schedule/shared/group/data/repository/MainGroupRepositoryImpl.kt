package com.example.schedule.shared.group.data.repository

import com.example.schedule.shared.group.domain.repository.MainGroupRepository

class MainGroupRepositoryImpl : MainGroupRepository {
    override suspend fun getMainGroup(): String {
        return "441, 442"
    }
}