package com.example.schedule.feature.schedule.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.schedule.feature.schedule.presentation.State

@Composable
fun Render(state: State, onSelectedScheduleIndexChangedListener: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        AnimatedContent(
            targetState = state,
            contentKey = { state::class.java },
        ) { state ->
            when (state) {
                is State.Initial, State.Loading -> LoadingContent()
                is State.Content -> Content(state, onSelectedScheduleIndexChangedListener)
            }
        }
    }
}

@Composable
private fun Content(state: State.Content, onSelectedScheduleIndexChangedListener: (Int) -> Unit) {
    val pagerState = rememberPagerState(
        initialPage = state.selectedScheduleIndex,
        pageCount = { state.scheduleStateList.size }
    )

    LaunchedEffect(state.selectedScheduleIndex) {
        pagerState.animateScrollToPage(state.selectedScheduleIndex)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            onSelectedScheduleIndexChangedListener(it)
        }
    }

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            Column(Modifier.fillMaxSize()) {
                Header(state.scheduleStateList[page].date, state.group.name)
                ScheduleDay(scheduleState = state.scheduleStateList[page])
            }
        }
    }
}