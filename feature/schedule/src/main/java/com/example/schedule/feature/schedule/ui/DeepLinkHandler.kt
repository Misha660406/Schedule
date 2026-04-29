package com.example.schedule.feature.schedule.ui

import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow

object DeepLinkHandler {
    // Хранит дату для перехода, пока UI её не обработает
    val targetDateFlow = MutableStateFlow<String?>(null)

    fun handleIntent(intent: Intent?) {
        val date = intent?.getStringExtra("TARGET_DATE")
        if (date != null) {
            targetDateFlow.value = date
            // Удаляем дату из интента, чтобы при повороте экрана нас снова не перекинуло
            intent.removeExtra("TARGET_DATE")
        }
    }
}