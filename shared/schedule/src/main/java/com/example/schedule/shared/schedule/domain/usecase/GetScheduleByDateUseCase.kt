package com.example.schedule.shared.schedule.domain.usecase

import com.example.schedule.shared.schedule.domain.entity.Schedule
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import java.time.LocalDate

class GetScheduleByDateUseCase(private val repository: ScheduleRepository) {

    suspend operator fun invoke(groupId: Long, date: LocalDate): Schedule =
        repository.getByDate(groupId, date)
}