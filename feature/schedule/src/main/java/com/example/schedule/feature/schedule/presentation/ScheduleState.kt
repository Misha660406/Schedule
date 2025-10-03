package com.example.schedule.feature.schedule.presentation

import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.schedule.domain.entity.Lesson
import java.time.LocalDate

sealed interface State {

    data object Initial : State

    data object Loading : State

    data class Content(
        val selectedGroup: Group,
        val selectedGroupList: List<Group>,
        val selectedGroupState: SelectedGroupState,
        val scheduleStateList: List<ScheduleState>,
        val selectedScheduleIndex: Int,
    ) : State
}

sealed interface ScheduleState {

    val date: LocalDate

    data class ReadyToLoad(override val date: LocalDate) : ScheduleState

    data class Loading(override val date: LocalDate) : ScheduleState

    data class Loaded(override val date: LocalDate, val lessons: List<Lesson>) : ScheduleState
}

enum class SelectedGroupState {

    SELECTING,
    SELECTED
}