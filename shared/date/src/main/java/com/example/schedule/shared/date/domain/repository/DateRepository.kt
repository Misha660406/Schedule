package com.example.schedule.shared.date.domain.repository

import java.time.LocalDate

interface DateRepository {

    fun getToday(): LocalDate

    fun getNextDate(date: LocalDate): LocalDate

    fun getPreviousDate(date: LocalDate): LocalDate
}
