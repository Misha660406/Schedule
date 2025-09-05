package com.example.schedule.feature.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.shared.date.domain.usecase.GetTodayUseCase
import com.example.schedule.shared.group.domain.usecase.GetSelectedGroupListUseCase
import com.example.schedule.shared.schedule.domain.usecase.GetScheduleByDateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val getTodayUseCase: GetTodayUseCase,
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
            val selectedGroupList = getSelectedGroupListUseCase()
            val defaultGroup = selectedGroupList.first()

            _state.value = State.Content(
                group = defaultGroup,
                schedule = getScheduleByDateUseCase(defaultGroup.id, today)
            )
        }
    }
}