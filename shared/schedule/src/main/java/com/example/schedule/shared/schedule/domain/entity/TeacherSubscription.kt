package com.example.schedule.shared.schedule.domain.entity

enum class Subgroup {
    ALL,
    FIRST,
    SECOND
}

data class TeacherSubscription(
    val groupName: String,
    val subjectName: String,
    val subgroup: Subgroup = Subgroup.ALL,
)