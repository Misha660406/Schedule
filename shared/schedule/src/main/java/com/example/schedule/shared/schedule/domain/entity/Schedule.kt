package com.example.schedule.shared.schedule.domain.entity

import java.time.LocalDate

data class Schedule(
    val date: LocalDate,
    val lessons: List<Lesson>
)
