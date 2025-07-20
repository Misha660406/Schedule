package com.example.schedule.shared.schedule.domain.usecase

import com.example.schedule.shared.schedule.domain.entity.Schedule
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import java.util.Date

class GetScheduleByDateUseCase(private val repository: ScheduleRepository) {

    suspend operator fun invoke(date: Date): Schedule =
        repository.getByDate(date)
}