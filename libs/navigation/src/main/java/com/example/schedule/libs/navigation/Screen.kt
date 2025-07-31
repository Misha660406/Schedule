package com.example.schedule.libs.navigation

import androidx.compose.runtime.Composable

interface Screen {

    @Composable
    fun Render()

    fun handleResult(result: Any) {
    }
}