package com.example.schedule.shared.group.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class GroupResponse(
    val status: String,
    val data: List<GroupDto>,
)

data class GroupDto(
    val id: Long,
    @SerializedName("number")
    val name: String,
)

interface GroupApi {
    @GET("http://80.89.199.85:8081/api/groups")
    suspend fun getGroups(): GroupResponse
}