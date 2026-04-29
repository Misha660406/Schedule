package com.example.schedule.feature.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.feature.schedule.ui.DeepLinkHandler.targetDateFlow
import com.example.schedule.shared.date.domain.usecase.GetDatesAroundTodayUseCase
import com.example.schedule.shared.date.domain.usecase.GetTodayUseCase
import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.usecase.GetMainGroupUseCase
import com.example.schedule.shared.group.domain.usecase.GetSelectedGroupListUseCase
import com.example.schedule.shared.schedule.domain.usecase.GetScheduleByDateUseCase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern

class ScheduleViewModel(
    private val getTodayUseCase: GetTodayUseCase,
    private val getSelectedGroupListUseCase: GetSelectedGroupListUseCase,
    private val getScheduleByDateUseCase: GetScheduleByDateUseCase,
    private val getDatesAroundTodayUseCase: GetDatesAroundTodayUseCase,
    private val getMainGroupUseCase: GetMainGroupUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Initial)
    val state: StateFlow<State> = _state

    fun loadInitialData() {
        if (_state.value != State.Initial) {
            return
        }

        viewModelScope.launch {
            _state.value = State.Loading

            val mainGroup = getMainGroupUseCase()
            val cleanGroup = mainGroup.replace(Regex("[/,\\s()]+"), "_").trimEnd('_')
            val topicName = "group_$cleanGroup"

            FirebaseMessaging.getInstance().subscribeToTopic(topicName).addOnCompleteListener {}
            val today = getTodayUseCase()
            val selectedGroupList = getSelectedGroupListUseCase()
            val scheduleStateList = createInitialScheduleStates()

            val dateToSelect = try {
                targetDateFlow.value?.let {
                    LocalDate.parse(it, ofPattern("dd.MM.yyyy"))
                }
            } catch (e: Exception) {
                null
            } ?: today
            targetDateFlow.value = null

            val initialIndex = scheduleStateList.indexOfFirst { it.date == dateToSelect }

            val safeIndex = if (initialIndex != -1) {
                initialIndex
            } else {
                scheduleStateList.indexOfFirst { it.date == today }.coerceAtLeast(0)
            }

            _state.value = State.Content(
                selectedGroup = selectedGroupList.first(),
                scheduleStateList = scheduleStateList,
                selectedScheduleIndex = safeIndex,
                selectedGroupList = selectedGroupList,
                selectedGroupState = SelectedGroupState.SELECTED
            )

            loadSchedule(safeIndex)
        }
    }

    fun updateSelectedScheduleIndex(newIndex: Int) {
        _state.update { current ->
            if (current is State.Content) current.copy(selectedScheduleIndex = newIndex) else current
        }
        loadSchedule(newIndex)
    }


    fun getPreviousDay() {
        val contentState = _state.value as? State.Content ?: return
        updateSelectedScheduleIndex(contentState.selectedScheduleIndex - 1)
    }

    fun getNextDay() {
        val contentState = _state.value as? State.Content ?: return
        updateSelectedScheduleIndex(contentState.selectedScheduleIndex + 1)
    }

    fun selectNewGroup(group: Group) {
        val current = _state.value as? State.Content ?: return
        if (current.selectedGroup.id == group.id) {
            cancelGroupSelecting()
            return
        }

        val newScheduleStateList = createInitialScheduleStates()
        _state.update { state ->
            if (state is State.Content) {
                state.copy(
                    selectedGroup = group,
                    selectedGroupState = SelectedGroupState.SELECTED,
                    scheduleStateList = newScheduleStateList
                )
            } else state
        }
        loadSchedule(current.selectedScheduleIndex)
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
        val scheduleState = currentState.scheduleStateList.getOrNull(index) ?: return

        if (scheduleState is ScheduleState.Loading || scheduleState is ScheduleState.Loaded) return

        val currentGroupName = currentState.selectedGroup.name

        _state.update { state ->
            if (state is State.Content) state.updateScheduleState(
                index,
                ScheduleState.Loading(scheduleState.date)
            ) else state
        }

        viewModelScope.launch {
            val schedule = getScheduleByDateUseCase(currentGroupName, scheduleState.date)

            _state.update { state ->
                if (state is State.Content && state.selectedGroup.name == currentGroupName) {
                    state.updateScheduleState(
                        index,
                        ScheduleState.Loaded(scheduleState.date, schedule.lessons)
                    )
                } else state
            }
        }
    }

    private fun State.Content.updateScheduleState(
        index: Int,
        scheduleState: ScheduleState,
    ): State.Content =
        scheduleStateList.toMutableList()
            .apply { set(index, scheduleState) }
            .let { copy(scheduleStateList = it) }

    fun selectDate(date: LocalDate) {
        val contentState = _state.value as? State.Content ?: return
        val index = contentState.scheduleStateList.indexOfFirst { it.date == date }
        if (index != -1) {
            updateSelectedScheduleIndex(index)
        }
    }
}