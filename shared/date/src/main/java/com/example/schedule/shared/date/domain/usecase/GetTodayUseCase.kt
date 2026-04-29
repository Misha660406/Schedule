package com.example.schedule.shared.date.domain.usecase

import com.example.schedule.shared.date.domain.repository.DateRepository
import java.time.LocalDate

class GetTodayUseCase(private val repository: DateRepository) {

    operator fun invoke(): LocalDate =
        repository.getToday()
}