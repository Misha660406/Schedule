package com.example.schedule.feature.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.shared.date.domain.usecase.GetNextDateUseCase
import com.example.schedule.shared.date.domain.usecase.GetPreviousDateUseCase
import com.example.schedule.shared.date.domain.usecase.GetTodayUseCase
import com.example.schedule.shared.group.domain.usecase.GetSelectedGroupListUseCase
import com.example.schedule.shared.schedule.domain.usecase.GetScheduleByDateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleViewModel(
    private val getTodayUseCase: GetTodayUseCase,
    private val getNextDateUseCase: GetNextDateUseCase,
    private val getPreviousDateUseCase: GetPreviousDateUseCase,
    private val getSelectedGroupListUseCase: GetSelectedGroupListUseCase,
    private val getScheduleByDateUseCase: GetScheduleByDateUseCase
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
            val group = getSelectedGroupListUseCase().first()
            val scheduleStateList = createInitialScheduleStates(today)

            _state.value = State.Content(
                group = group,
                scheduleStateList = scheduleStateList,
                selectedScheduleIndex = scheduleStateList.indexOfFirst { it.date == today },
            )
        }
    }

    private fun createInitialScheduleStates(today: LocalDate): List<ScheduleState> =
        getCurrentWeek(today).map(ScheduleState::ReadyToLoad)

    private fun getCurrentWeek(today: LocalDate): List<LocalDate> {
        val datesOfWeek = mutableListOf(getStartOfCurrentWeek(today))
        repeat(6) {
            datesOfWeek.add(getNextDateUseCase(datesOfWeek.last()))
        }
        return datesOfWeek
    }

    private fun getStartOfCurrentWeek(today: LocalDate): LocalDate {
        var startOfWeek = today
        repeat(startOfWeek.dayOfWeek.value - 1) {
            startOfWeek = getPreviousDateUseCase(startOfWeek)
        }
        return startOfWeek
    }

    fun updateSelectedScheduleIndex(newIndex: Int) {
        val contentState = _state.value as? State.Content ?: return
        _state.value = contentState.copy(selectedScheduleIndex = newIndex)
        loadSchedule(newIndex)
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
            val schedule = getScheduleByDateUseCase(currentState.group.id, scheduleState.date)
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