package com.example.schedule.shared.date.di

import com.example.schedule.shared.date.data.repository.DateRepositoryImpl
import com.example.schedule.shared.date.domain.repository.DateRepository
import com.example.schedule.shared.date.domain.usecase.GetNextDateUseCase
import com.example.schedule.shared.date.domain.usecase.GetPreviousDateUseCase
import com.example.schedule.shared.date.domain.usecase.GetTodayUseCase
import org.koin.dsl.module

val sharedDateModule = module {
    single<DateRepository> { DateRepositoryImpl() }

    factory { GetNextDateUseCase(repository = get()) }
    factory { GetPreviousDateUseCase(repository = get()) }
    factory { GetTodayUseCase(repository = get()) }
}