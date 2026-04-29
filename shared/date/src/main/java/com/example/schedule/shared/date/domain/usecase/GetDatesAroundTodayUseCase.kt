package com.example.schedule.shared.date.domain.usecase

import com.example.schedule.shared.date.domain.repository.DateRepository
import java.time.LocalDate

class GetDatesAroundTodayUseCase(private val repository: DateRepository) {

    operator fun invoke(daysBack: Long, daysForward: Long): List<LocalDate> {
        val today = repository.getToday()
        return (-daysBack..daysForward).map { repository.plusDays(today, it) }
    }
}