package com.example.schedule.shared.group.data.repository

import retrofit2.http.GET
import retrofit2.http.Path

interface SubjectApi {
    @GET("api/subjects/{group_name}")
    suspend fun getGroupSubjects(@Path("group_name") groupName: String): List<String>
}