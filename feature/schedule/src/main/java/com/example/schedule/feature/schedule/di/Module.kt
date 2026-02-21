package com.example.schedule.feature.schedule.di

import com.example.schedule.feature.schedule.presentation.ScheduleViewModel
import com.example.schedule.feature.schedule.presentation.TeacherScheduleViewModel
import com.example.schedule.shared.schedule.data.repository.TeacherScheduleRepositoryImpl
import com.example.schedule.shared.schedule.domain.repository.TeacherScheduleRepository
import com.example.schedule.shared.schedule.domain.usecase.GetTeacherScheduleUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureScheduleModule = module {
    single<TeacherScheduleRepository> { TeacherScheduleRepositoryImpl(get()) }
    factory { GetTeacherScheduleUseCase(get()) }

    viewModel {
        TeacherScheduleViewModel(
            getTodayUseCase = get(),
            getDatesAroundTodayUseCase = get(),
            getTeacherScheduleUseCase = get()
        )
    }

    viewModel {
        ScheduleViewModel(
            getTodayUseCase = get(),
            getSelectedGroupListUseCase = get(),
            getScheduleByDateUseCase = get(),
            getDatesAroundTodayUseCase = get(),
        )
    }
}