package com.example.schedule.shared.date.data.repository

import com.example.schedule.shared.date.domain.repository.DateRepository
import java.time.LocalDate

class DateRepositoryImpl : DateRepository {

    override fun getToday(): LocalDate =
        LocalDate.now()

    override fun getNextDate(date: LocalDate): LocalDate {
        return date.plusDays(1)
    }

    override fun getPreviousDate(date: LocalDate): LocalDate {
        return date.minusDays(1)
    }
}