package com.example.schedule.shared.date.domain.usecase

import com.example.schedule.shared.date.domain.repository.DateRepository
import java.util.Date

class GetTodayUseCase(private val repository: DateRepository) {

    operator fun invoke(): Date =
        repository.getToday()
}