package com.example.schedule

import android.app.Application
import com.example.schedule.di.navigationModule
import com.example.schedule.feature.schedule.di.featureScheduleModule
import com.example.schedule.shared.date.di.sharedDateModule
import com.example.schedule.shared.group.di.sharedGroupModule
import com.example.schedule.shared.schedule.di.sharedScheduleModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                navigationModule,
                sharedDateModule,
                sharedGroupModule,
                sharedScheduleModule,
                featureScheduleModule
            )
        }
    }
}