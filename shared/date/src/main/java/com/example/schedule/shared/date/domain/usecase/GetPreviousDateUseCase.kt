package com.example.schedule.shared.date.domain.usecase

import com.example.schedule.shared.date.domain.repository.DateRepository
import java.util.Date

class GetPreviousDateUseCase(private val repository: DateRepository) {

    operator fun invoke(date: Date): Date =
        repository.getPreviousDate(date)
}