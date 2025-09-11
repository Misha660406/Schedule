package com.example.schedule.feature.schedule.di

import com.example.schedule.feature.schedule.presentation.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureScheduleModule = module {
    viewModel {
        ScheduleViewModel(
            getTodayUseCase = get(),
            getNextDateUseCase = get(),
            getPreviousDateUseCase = get(),
            getSelectedGroupListUseCase = get(),
            getScheduleByDateUseCase = get()
        )
    }
}