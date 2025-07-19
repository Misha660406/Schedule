package com.example.schedule.shared.date.domain.repository

import java.util.Date

interface DateRepository {

    fun getToday(): Date

    fun getNextDate(date: Date): Date

    fun getPreviousDate(date: Date): Date
}
