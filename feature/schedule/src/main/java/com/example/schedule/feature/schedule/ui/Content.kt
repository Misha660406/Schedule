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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.schedule.feature.schedule.presentation.State
import com.example.schedule.shared.group.domain.entity.Group
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun ScheduleContent(
    state: State,
    onSelectedScheduleIndexChangedListener: (Int) -> Unit,
    onOpenGroupSelectorListener: () -> Unit,
    onGroupSelectedListener: (Group) -> Unit,
    onCloseGroupSelectorListener: () -> Unit,
    onPreviousDayListener: () -> Unit,
    onNextDayListener: () -> Unit,
    onDateSelectedListener: (LocalDate) -> Unit,
    onSettingsClickListener: (() -> Unit)? = null,
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
                    onPreviousDayListener = onPreviousDayListener,
                    onNextDayListener = onNextDayListener,
                    onDateSelectedListener = onDateSelectedListener,
                    onSettingsClickListener = onSettingsClickListener // ПРОБРАСЫВАЕМ ДАЛЬШЕ
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: State.Content,
    onSelectedScheduleIndexChangedListener: (Int) -> Unit,
    onOpenGroupSelectorListener: () -> Unit,
    onGroupSelectedListener: (Group) -> Unit,
    onCloseGroupSelectorListener: () -> Unit,
    onPreviousDayListener: () -> Unit,
    onNextDayListener: () -> Unit,
    onDateSelectedListener: (LocalDate) -> Unit,
    onSettingsClickListener: (() -> Unit)? = null, // ДОБАВИЛИ ПАРАМЕТР СЮДА
) {
    var showDatePicker by remember { mutableStateOf(false) }

    GroupSelectorBottomSheet(
        state = state,
        onGroupSelected = onGroupSelectedListener,
        onCloseGroupSelector = onCloseGroupSelectorListener
    )

    val pagerState = rememberDayPagerState(
        state = state,
        onSelectedScheduleIndexChangedListener = onSelectedScheduleIndexChangedListener
    )

    val currentDate = state.scheduleStateList[pagerState.currentPage].date

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentDate.atStartOfDay(ZoneOffset.UTC).toInstant()
                .toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                        onDateSelectedListener(selectedDate)
                    }
                    showDatePicker = false
                }) { Text("ОК") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Отмена") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(Modifier.fillMaxSize()) {
        Header(
            date = currentDate,
            groupName = state.selectedGroup.name,
            selectedGroupState = state.selectedGroupState,
            onGroupSelectionClick = onOpenGroupSelectorListener,
            onPreviousDayClick = onPreviousDayListener,
            onNextDayClick = onNextDayListener,
            onDateClick = { showDatePicker = true },
            onSettingsClick = onSettingsClickListener
        )
        HorizontalPager(state = pagerState) { page ->
            ScheduleDay(scheduleState = state.scheduleStateList[page])
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

    val currentSelectedIndex by rememberUpdatedState(state.selectedScheduleIndex)

    LaunchedEffect(state.selectedScheduleIndex) {
        if (pagerState.currentPage != state.selectedScheduleIndex) {
            pagerState.scrollToPage(state.selectedScheduleIndex)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { settledPage ->
            if (settledPage != currentSelectedIndex) {
                onSelectedScheduleIndexChangedListener(settledPage)
            }
        }
    }
    return pagerState
}