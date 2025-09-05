package com.example.schedule.feature.schedule.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.schedule.feature.schedule.R
import com.example.schedule.feature.schedule.presentation.State

@Composable
fun Render(state: State) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Crossfade(targetState = state) { state ->
            when (state) {
                is State.Initial, State.Loading -> LoadingContent()
                is State.Content -> Content(state)
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.feature_schedule_loading)
        )
    }
}

@Composable
private fun Content(state: State.Content) {
    Column(Modifier.fillMaxSize()) {
        Header(state.schedule.date, state.group.name)
        ScheduleDay(schedule = state.schedule)
    }
}