package com.example.schedule.feature.schedule.di

import com.example.schedule.feature.schedule.presentation.ScheduleViewModel
import com.example.schedule.feature.schedule.presentation.TeacherScheduleViewModel
import com.example.schedule.feature.schedule.presentation.TeacherSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureScheduleModule = module {

    viewModel {
        TeacherSettingsViewModel(
            getAllGroupListUseCase = get(),
            getGroupSubjectsUseCase = get(),
            preferencesRepository = get()
        )
    }

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
            getMainGroupUseCase = get(),
        )
    }
}