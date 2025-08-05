package com.example.schedule.feature.schedule.di

import com.example.schedule.feature.schedule.presentation.ScheduleViewModel
import com.example.schedule.shared.date.data.repository.DateRepositoryImpl
import com.example.schedule.shared.date.domain.repository.DateRepository
import com.example.schedule.shared.group.data.repository.GroupRepositoryImpl
import com.example.schedule.shared.group.data.repository.SelectedGroupRepositoryImpl
import com.example.schedule.shared.group.domain.repository.GroupRepository
import com.example.schedule.shared.group.domain.repository.SelectedGroupRepository
import com.example.schedule.shared.schedule.data.repository.ScheduleRepositoryImpl
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureScheduleModule = module {

    single<DateRepository> { DateRepositoryImpl() }
    single<GroupRepository> { GroupRepositoryImpl() }
    single<SelectedGroupRepository> { SelectedGroupRepositoryImpl() }
    single<ScheduleRepository> { ScheduleRepositoryImpl() }

    viewModel {
        ScheduleViewModel(
            dateRepo = get(),
            selectedGroupRepo = get(),
            scheduleRepo = get(),
        )
    }
}

