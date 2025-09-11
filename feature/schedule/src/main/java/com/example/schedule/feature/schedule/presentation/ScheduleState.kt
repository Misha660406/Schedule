package com.example.schedule.feature.schedule.presentation

import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.schedule.domain.entity.Schedule

sealed interface State {

    data object Initial : State

    data object Loading : State

    data class Content(
        val group: Group,
        val schedule: List<Schedule>,
        val selectedScheduleIndex: Int,
    ) : State
}