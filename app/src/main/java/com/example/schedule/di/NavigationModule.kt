package com.example.schedule.di

import com.example.schedule.libs.navigation.BackstackNavigator
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.dsl.module

object GlobalBackstackNavigatorQualifier : Qualifier {

    override val value: QualifierValue = "GlobalBackstackNavigatorQualifier"
}

val navigationModule = module {
    single(GlobalBackstackNavigatorQualifier) { BackstackNavigator() }
}