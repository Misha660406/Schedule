package com.example.schedule.shared.date.domain.usecase

import com.example.schedule.shared.date.domain.repository.DateRepository
import java.util.Date

class GetNextDateUseCase(private val repository: DateRepository) {

    operator fun invoke(date: Date): Date =
        repository.getNextDate(date)
}