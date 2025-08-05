package com.example.schedule.feature.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.shared.date.domain.repository.DateRepository
import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.repository.SelectedGroupRepository
import com.example.schedule.shared.schedule.domain.entity.Lesson
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

sealed class ScheduleState {
    data object Initial : ScheduleState()
    data object Loading : ScheduleState()
    data class Content(val schedule: List<Lesson>, val date: LocalDate) : ScheduleState()
    data class ContentWithSelected(
        val schedule: List<Lesson>,
        val date: LocalDate,
        val group: Group
    ) : ScheduleState()
}

class ScheduleViewModel(
    private val dateRepo: DateRepository,
    private val selectedGroupRepo: SelectedGroupRepository,
    private val scheduleRepo: ScheduleRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ScheduleState>(ScheduleState.Initial)
    val state: StateFlow<ScheduleState> = _state

    fun init() {
        viewModelScope.launch {
            _state.value = ScheduleState.Loading
            val today = dateRepo.getToday()
            val groups = selectedGroupRepo.getSelectedGroupList()

            if (groups.isEmpty()) {
                _state.value = ScheduleState.Content(emptyList(), today)
            } else {
                val schedule = loadScheduleForGroups(groups, today)
                _state.value = ScheduleState.ContentWithSelected(schedule, today, groups.first())
            }
        }
    }

    fun startSelectingGroup() {
        _state.value = ScheduleState.Initial
    }

    fun loadNextDate() {
        val currentDate = getCurrentDate()
        viewModelScope.launch {
            _state.value = ScheduleState.Loading
            val nextDate = getNextWorkingDate(currentDate)
            loadScheduleForDate(nextDate)
        }
    }

    fun loadPreviousDate() {
        val currentDate = getCurrentDate()
        viewModelScope.launch {
            _state.value = ScheduleState.Loading
            val prevDate = getPreviousWorkingDate(currentDate)
            loadScheduleForDate(prevDate)
        }
    }

    private fun getCurrentDate(): LocalDate {
        return (_state.value as? ScheduleState.ContentWithSelected)?.date ?: dateRepo.getToday()
    }

    private fun getNextWorkingDate(current: LocalDate): LocalDate {
        return if (current.dayOfWeek == DayOfWeek.SATURDAY) {
            dateRepo.getNextDate(dateRepo.getNextDate(current))
        } else {
            dateRepo.getNextDate(current)
        }
    }

    private fun getPreviousWorkingDate(current: LocalDate): LocalDate {
        return if (current.dayOfWeek == DayOfWeek.MONDAY) {
            dateRepo.getPreviousDate(dateRepo.getPreviousDate(current))
        } else {
            dateRepo.getPreviousDate(current)
        }
    }

    private suspend fun loadScheduleForDate(date: LocalDate) {
        val groups = selectedGroupRepo.getSelectedGroupList()
        val schedule = loadScheduleForGroups(groups, date)
        _state.value = ScheduleState.ContentWithSelected(schedule, date, groups.first())
    }

    private suspend fun loadScheduleForGroups(groups: List<Group>, date: LocalDate): List<Lesson> {
        return groups.flatMap { group ->
            scheduleRepo.getByDate(group.id, date).lessons
        }
    }
}