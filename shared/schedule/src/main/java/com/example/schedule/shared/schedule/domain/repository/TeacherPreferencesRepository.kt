package com.example.schedule.shared.schedule.domain.repository

import com.example.schedule.shared.schedule.domain.entity.TeacherSubscription
import kotlinx.coroutines.flow.Flow

interface TeacherPreferencesRepository {
    fun getSubscriptionsFlow(): Flow<List<TeacherSubscription>>
    suspend fun getSubscriptions(): List<TeacherSubscription>
    suspend fun saveSubscriptions(subscriptions: List<TeacherSubscription>)
}