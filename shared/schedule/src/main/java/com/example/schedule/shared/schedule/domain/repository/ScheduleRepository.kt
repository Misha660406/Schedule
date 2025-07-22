package com.example.schedule.shared.schedule.domain.repository

import com.example.schedule.shared.schedule.domain.entity.Schedule
import java.util.Date

interface ScheduleRepository {

    suspend fun getByDate(date: Date): Schedule
}
