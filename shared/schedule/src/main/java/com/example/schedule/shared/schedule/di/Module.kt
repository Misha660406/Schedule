package com.example.schedule.shared.schedule.di

import com.example.schedule.shared.schedule.data.repository.ScheduleRepositoryImpl
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import com.example.schedule.shared.schedule.domain.usecase.GetScheduleByDateUseCase
import org.koin.dsl.module

val sharedScheduleModule = module {
    single<ScheduleRepository> { ScheduleRepositoryImpl() }

    factory { GetScheduleByDateUseCase(repository = get()) }
}