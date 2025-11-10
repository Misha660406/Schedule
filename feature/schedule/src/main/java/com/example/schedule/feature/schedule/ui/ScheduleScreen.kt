package com.example.schedule.feature.schedule.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.schedule.feature.schedule.presentation.ScheduleViewModel
import com.example.schedule.libs.navigation.Screen
import org.koin.java.KoinJavaComponent.inject

class ScheduleScreen : Screen {

    private val viewModel: ScheduleViewModel by inject(ScheduleViewModel::class.java)

    @Composable
    override fun Render() {
        val state by viewModel.state.collectAsState()

        Render(
            state = state,
            onSelectedScheduleIndexChangedListener = viewModel::updateSelectedScheduleIndex,
            onOpenGroupSelectorListener = viewModel::startGroupSelecting,
            onCloseGroupSelectorListener = viewModel::cancelGroupSelecting,
            onGroupSelectedListener = viewModel::selectNewGroup,
            onPreviousDayListener = viewModel::getPreviousDay,
            onNextDayListener = viewModel::getNextDay,
        )

        LaunchedEffect(Unit) {
            viewModel.loadInitialData()
        }
    }
}