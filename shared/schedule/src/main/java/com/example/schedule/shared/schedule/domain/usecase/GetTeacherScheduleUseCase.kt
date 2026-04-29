package com.example.schedule.shared.schedule.domain.usecase

import com.example.schedule.shared.schedule.domain.entity.Schedule
import com.example.schedule.shared.schedule.domain.repository.TeacherScheduleRepository
import java.time.LocalDate

class GetTeacherScheduleUseCase(
    private val repository: TeacherScheduleRepository
) {
    suspend operator fun invoke(date: LocalDate): Schedule {
        return repository.getTeacherSchedule(date)
    }
}