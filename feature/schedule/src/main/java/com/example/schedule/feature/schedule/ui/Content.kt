package com.example.schedule.feature.schedule.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.schedule.feature.schedule.presentation.State
import com.example.schedule.shared.group.domain.entity.Group

@Composable
fun Render(
    state: State,
    onSelectedScheduleIndexChangedListener: (Int) -> Unit,
    onOpenGroupSelectorListener: () -> Unit,
    onGroupSelectedListener: (Group) -> Unit,
    onCloseGroupSelectorListener: () -> Unit,
) {
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
                is State.Content -> Content(
                    state = state,
                    onSelectedScheduleIndexChangedListener = onSelectedScheduleIndexChangedListener,
                    onOpenGroupSelectorListener = onOpenGroupSelectorListener,
                    onGroupSelectedListener = onGroupSelectedListener,
                    onCloseGroupSelectorListener = onCloseGroupSelectorListener,
                )
            }
        }
    }
}

@Composable
private fun Content(
    state: State.Content,
    onSelectedScheduleIndexChangedListener: (Int) -> Unit,
    onOpenGroupSelectorListener: () -> Unit,
    onGroupSelectedListener: (Group) -> Unit,
    onCloseGroupSelectorListener: () -> Unit,
) {
    GroupSelectorBottomSheet(
        state = state,
        onGroupSelected = onGroupSelectedListener,
        onCloseGroupSelector = onCloseGroupSelectorListener
    )

    val pagerState = rememberDayPagerState(
        state = state,
        onSelectedScheduleIndexChangedListener = onSelectedScheduleIndexChangedListener
    )

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            Column(Modifier.fillMaxSize()) {
                Header(
                    date = state.scheduleStateList[page].date,
                    groupName = state.selectedGroup.name,
                    onGroupSelectionClick = onOpenGroupSelectorListener
                )
                ScheduleDay(scheduleState = state.scheduleStateList[page])
            }
        }
    }
}

@Composable
private fun rememberDayPagerState(
    state: State.Content,
    onSelectedScheduleIndexChangedListener: (Int) -> Unit
): PagerState {
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
    return pagerState
}