package com.example.schedule.shared.date.domain.usecase

import com.example.schedule.shared.date.domain.repository.DateRepository
import java.time.LocalDate

class GetPreviousDateUseCase(private val repository: DateRepository) {

    operator fun invoke(date: LocalDate): LocalDate =
        repository.getPreviousDate(date)
}