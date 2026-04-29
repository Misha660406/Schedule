package com.example.schedule.shared.schedule.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class LessonDto(
    val position: Int,
    val name: String,
    val room: String,
)

interface ScheduleApi {
    @GET("api/schedule/{group_name}")
    suspend fun getSchedule(
        @Path("group_name") groupName: String,
        @Query("date") date: String,
    ): List<LessonDto>
}