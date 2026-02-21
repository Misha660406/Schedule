package com.example.schedule.feature.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.shared.date.domain.usecase.GetDatesAroundTodayUseCase
import com.example.schedule.shared.date.domain.usecase.GetTodayUseCase
import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.schedule.domain.usecase.GetTeacherScheduleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeacherScheduleViewModel(
    private val getTodayUseCase: GetTodayUseCase,
    private val getDatesAroundTodayUseCase: GetDatesAroundTodayUseCase,
    private val getTeacherScheduleUseCase: GetTeacherScheduleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Initial)
    val state: StateFlow<State> = _state

    private val teacherGroup = Group(-1, "Преподаватель")

    fun loadInitialData() {
        if (_state.value != State.Initial) return

        viewModelScope.launch {
            _state.value = State.Loading

            val today = getTodayUseCase()
            val scheduleStateList = getDatesAroundTodayUseCase(500, 500)
                .map { ScheduleState.ReadyToLoad(it) }

            val initialIndex = scheduleStateList.indexOfFirst { it.date == today }

            _state.value = State.Content(
                selectedGroup = teacherGroup,
                selectedGroupList = emptyList(), // Учителю не нужно выбирать группы
                selectedGroupState = SelectedGroupState.SELECTED,
                scheduleStateList = scheduleStateList,
                selectedScheduleIndex = if (initialIndex != -1) initialIndex else 0
            )

            if (initialIndex != -1) {
                loadSchedule(initialIndex)
            }
        }
    }

    fun updateSelectedScheduleIndex(newIndex: Int) {
        val content = _state.value as? State.Content ?: return
        _state.value = content.copy(selectedScheduleIndex = newIndex)
        loadSchedule(newIndex)
    }

    fun getPreviousDay() {
        val content = _state.value as? State.Content ?: return
        updateSelectedScheduleIndex(content.selectedScheduleIndex - 1)
    }

    fun getNextDay() {
        val content = _state.value as? State.Content ?: return
        updateSelectedScheduleIndex(content.selectedScheduleIndex + 1)
    }

    fun startGroupSelecting() {}
    fun cancelGroupSelecting() {}
    fun selectNewGroup(group: Group) {}

    private fun loadSchedule(index: Int) {
        val content = _state.value as? State.Content ?: return
        val scheduleState = content.scheduleStateList.getOrNull(index) ?: return

        if (scheduleState is ScheduleState.Loaded) return

        _state.value = content.updateScheduleState(
            index, ScheduleState.Loading(scheduleState.date)
        )

        viewModelScope.launch {
            // ТУТ ВЫЗЫВАЕМ НАШ НОВЫЙ USECASE
            val schedule = getTeacherScheduleUseCase(scheduleState.date)

            (_state.value as? State.Content)?.let { currentState ->
                _state.value = currentState.updateScheduleState(
                    index,
                    ScheduleState.Loaded(scheduleState.date, schedule.lessons)
                )
            }
        }
    }

    private fun State.Content.updateScheduleState(
        index: Int,
        newState: ScheduleState
    ): State.Content {
        val newList = scheduleStateList.toMutableList()
        newList[index] = newState
        return copy(scheduleStateList = newList)
    }
}