package com.example.schedule.shared.group.domain.repository

interface MainGroupRepository {

    suspend fun getMainGroup(): String
}