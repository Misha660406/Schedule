package com.example.schedule.feature.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.shared.date.domain.usecase.GetDatesAroundTodayUseCase
import com.example.schedule.shared.date.domain.usecase.GetTodayUseCase
import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.usecase.GetSelectedGroupListUseCase
import com.example.schedule.shared.schedule.domain.usecase.GetScheduleByDateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val getTodayUseCase: GetTodayUseCase,
    private val getSelectedGroupListUseCase: GetSelectedGroupListUseCase,
    private val getScheduleByDateUseCase: GetScheduleByDateUseCase,
    private val getDatesAroundTodayUseCase: GetDatesAroundTodayUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Initial)
    val state: StateFlow<State> = _state

    fun loadInitialData() {
        if (_state.value != State.Initial) {
            return
        }

        viewModelScope.launch {
            _state.value = State.Loading

            val today = getTodayUseCase()
            val selectedGroupList = getSelectedGroupListUseCase()
            val scheduleStateList = createInitialScheduleStates()

            _state.value = State.Content(
                selectedGroup = selectedGroupList.first(),
                scheduleStateList = scheduleStateList,
                selectedScheduleIndex = scheduleStateList.indexOfFirst { it.date == today },
                selectedGroupList = selectedGroupList,
                selectedGroupState = SelectedGroupState.SELECTED
            )
        }
    }

    fun updateSelectedScheduleIndex(newIndex: Int) {
        val contentState = _state.value as? State.Content ?: return
        _state.value = contentState.copy(selectedScheduleIndex = newIndex)
        loadSchedule(newIndex)
    }

    fun selectNewGroup(group: Group) {
        val contentState = _state.value as? State.Content ?: return

        if (contentState.selectedGroup.id == group.id) {
            cancelGroupSelecting()
            return
        }

        viewModelScope.launch {
            _state.value = contentState.copy(
                selectedGroup = group,
                selectedGroupState = SelectedGroupState.SELECTED,
                scheduleStateList = createInitialScheduleStates()
            )
            loadSchedule(contentState.selectedScheduleIndex)
        }
    }

    fun cancelGroupSelecting() {
        updateSelectedGroupState(SelectedGroupState.SELECTED)
    }

    fun startGroupSelecting() {
        updateSelectedGroupState(SelectedGroupState.SELECTING)
    }

    private fun updateSelectedGroupState(newState: SelectedGroupState) {
        val contentState = _state.value as? State.Content ?: return
        _state.value = contentState.copy(selectedGroupState = newState)
    }

    private fun createInitialScheduleStates(): List<ScheduleState> {
        return getDatesAroundTodayUseCase(500, 500)
            .map(ScheduleState::ReadyToLoad)
    }

    private fun loadSchedule(index: Int) {
        val currentState = _state.value as? State.Content ?: return
        val scheduleState = currentState.scheduleStateList[index]

        if (scheduleState is ScheduleState.Loading || scheduleState is ScheduleState.Loaded) {
            return
        }

        _state.value = currentState.updateScheduleState(
            index = index,
            scheduleState = ScheduleState.Loading(scheduleState.date)
        )

        viewModelScope.launch {
            val schedule = getScheduleByDateUseCase(
                currentState.selectedGroup.id,
                scheduleState.date,
            )
            (_state.value as? State.Content)?.let {
                _state.value = it.updateScheduleState(
                    index = index,
                    scheduleState = ScheduleState.Loaded(scheduleState.date, schedule.lessons)
                )
            }
        }
    }

    private fun State.Content.updateScheduleState(
        index: Int,
        scheduleState: ScheduleState
    ): State.Content =
        scheduleStateList.toMutableList()
            .apply { set(index, scheduleState) }
            .let { copy(scheduleStateList = it) }
}