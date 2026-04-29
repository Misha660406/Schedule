package com.example.schedule.shared.schedule.data.repository

import android.content.Context
import com.example.schedule.shared.schedule.domain.entity.TeacherSubscription
import com.example.schedule.shared.schedule.domain.repository.TeacherPreferencesRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class TeacherPreferencesRepositoryImpl(
    private val context: Context,
    private val gson: Gson,
) : TeacherPreferencesRepository {

    private val file = File(context.filesDir, "teacher_prefs.json")
    private val cacheFlow = MutableStateFlow<List<TeacherSubscription>>(emptyList())

    init {
        loadFromFile()
    }

    override fun getSubscriptionsFlow(): Flow<List<TeacherSubscription>> = cacheFlow

    override suspend fun getSubscriptions(): List<TeacherSubscription> = cacheFlow.value

    override suspend fun saveSubscriptions(subscriptions: List<TeacherSubscription>) {
        cacheFlow.value = subscriptions
        val json = gson.toJson(subscriptions)
        file.writeText(json)
    }

    private fun loadFromFile() {
        if (!file.exists()) return
        try {
            val json = file.readText()
            val type = object : TypeToken<List<TeacherSubscription>>() {}.type
            val data: List<TeacherSubscription> = gson.fromJson(json, type)
            cacheFlow.value = data
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}