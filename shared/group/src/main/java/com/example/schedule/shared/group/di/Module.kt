package com.example.schedule.shared.group.di

import com.example.schedule.shared.group.data.repository.GroupRepositoryImpl
import com.example.schedule.shared.group.data.repository.SelectedGroupRepositoryImpl
import com.example.schedule.shared.group.domain.repository.GroupRepository
import com.example.schedule.shared.group.domain.repository.SelectedGroupRepository
import com.example.schedule.shared.group.domain.usecase.GetAllGroupListUseCase
import com.example.schedule.shared.group.domain.usecase.GetSelectedGroupListUseCase
import com.example.schedule.shared.group.domain.usecase.RemoveSelectedGroupUseCase
import org.koin.dsl.module

val sharedGroupModule = module {
    single<GroupRepository> { GroupRepositoryImpl() }
    single<SelectedGroupRepository> { SelectedGroupRepositoryImpl() }

    factory { GetAllGroupListUseCase(repository = get()) }
    factory { GetSelectedGroupListUseCase(repository = get()) }
    factory { RemoveSelectedGroupUseCase(repository = get()) }
    factory { RemoveSelectedGroupUseCase(repository = get()) }
}