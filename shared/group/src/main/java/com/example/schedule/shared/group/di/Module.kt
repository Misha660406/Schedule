package com.example.schedule.shared.group.di

import com.example.schedule.shared.group.data.GroupApi
import com.example.schedule.shared.group.data.repository.GroupRepositoryImpl
import com.example.schedule.shared.group.data.repository.MainGroupRepositoryImpl
import com.example.schedule.shared.group.data.repository.SelectedGroupRepositoryImpl
import com.example.schedule.shared.group.data.repository.SubjectApi
import com.example.schedule.shared.group.data.repository.SubjectRepositoryImpl
import com.example.schedule.shared.group.domain.repository.GroupRepository
import com.example.schedule.shared.group.domain.repository.MainGroupRepository
import com.example.schedule.shared.group.domain.repository.SelectedGroupRepository
import com.example.schedule.shared.group.domain.repository.SubjectRepository
import com.example.schedule.shared.group.domain.usecase.GetAllGroupListUseCase
import com.example.schedule.shared.group.domain.usecase.GetGroupSubjectsUseCase
import com.example.schedule.shared.group.domain.usecase.GetMainGroupUseCase
import com.example.schedule.shared.group.domain.usecase.GetSelectedGroupListUseCase
import com.example.schedule.shared.group.domain.usecase.RemoveSelectedGroupUseCase
import org.koin.dsl.module
import retrofit2.Retrofit

val sharedGroupModule = module {
    single<GroupApi> { get<Retrofit>().create(GroupApi::class.java) }
    single<GroupRepository> { GroupRepositoryImpl(api = get()) }
    single<SelectedGroupRepository> { SelectedGroupRepositoryImpl(groupRepository = get()) }
    single<SubjectApi> { get<Retrofit>().create(SubjectApi::class.java) }
    single<SubjectRepository> { SubjectRepositoryImpl(api = get()) }
    single<MainGroupRepository> { MainGroupRepositoryImpl() }

    factory { GetGroupSubjectsUseCase(repository = get()) }
    factory { GetAllGroupListUseCase(repository = get()) }
    factory { GetSelectedGroupListUseCase(repository = get()) }
    factory { RemoveSelectedGroupUseCase(repository = get()) }
    factory { GetMainGroupUseCase(repository = get()) }
}