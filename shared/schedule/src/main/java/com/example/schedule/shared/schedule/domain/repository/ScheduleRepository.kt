package com.example.schedule.shared.schedule.domain.repository

import com.example.schedule.shared.schedule.domain.entity.Schedule
import java.time.LocalDate

interface ScheduleRepository {

    suspend fun getByDate(groupId: Long, date: LocalDate): Schedule
}
