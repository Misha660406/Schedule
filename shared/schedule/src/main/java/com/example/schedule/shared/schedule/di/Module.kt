package com.example.schedule.shared.schedule.di

import com.example.schedule.shared.schedule.data.ScheduleApi
import com.example.schedule.shared.schedule.data.repository.ScheduleRepositoryImpl
import com.example.schedule.shared.schedule.data.repository.TeacherPreferencesRepositoryImpl
import com.example.schedule.shared.schedule.data.repository.TeacherScheduleRepositoryImpl
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import com.example.schedule.shared.schedule.domain.repository.TeacherPreferencesRepository
import com.example.schedule.shared.schedule.domain.repository.TeacherScheduleRepository
import com.example.schedule.shared.schedule.domain.usecase.GetScheduleByDateUseCase
import com.example.schedule.shared.schedule.domain.usecase.GetTeacherScheduleUseCase
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val sharedScheduleModule = module {
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("http://147.124.204.18:8000/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ScheduleApi> { get<Retrofit>().create(ScheduleApi::class.java) }

    single<ScheduleRepository> { ScheduleRepositoryImpl(api = get()) }

    single { Gson() }

    single<TeacherPreferencesRepository> {
        TeacherPreferencesRepositoryImpl(context = androidContext(), gson = get())
    }
    single<TeacherScheduleRepository> {
        TeacherScheduleRepositoryImpl(scheduleRepository = get(), preferencesRepository = get())
    }

    factory { GetScheduleByDateUseCase(repository = get()) }
    factory { GetTeacherScheduleUseCase(repository = get()) }
}