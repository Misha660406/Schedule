package com.example.schedule.shared.schedule.domain.entity

import java.util.Date

data class Schedule(
    val date: Date,
    val lessons: List<Lesson>
)
