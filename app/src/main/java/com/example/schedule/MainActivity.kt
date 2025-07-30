package com.example.schedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.schedule.di.GlobalBackstackNavigatorQualifier
import com.example.schedule.libs.navigation.BackstackNavigator
import com.example.schedule.libs.navigation.BackstackNavigator.EmptyScreen
import com.example.schedule.ui.theme.ScheduleTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val navigator: BackstackNavigator by inject(GlobalBackstackNavigatorQualifier)

    init {
        navigator.open(EmptyScreen)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
        )
        setContent {
            ScheduleTheme(darkTheme = isSystemInDarkTheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val currentScreen by navigator.currentScreen.collectAsState()
                    currentScreen.Render()
                }
            }
        }
    }
}