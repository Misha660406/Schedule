package com.example.schedule.feature.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.group.domain.usecase.GetAllGroupListUseCase
import com.example.schedule.shared.group.domain.usecase.GetGroupSubjectsUseCase
import com.example.schedule.shared.schedule.domain.entity.Subgroup
import com.example.schedule.shared.schedule.domain.entity.TeacherSubscription
import com.example.schedule.shared.schedule.domain.repository.TeacherPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeacherSettingsState(
    val courses: Map<Int, List<Group>> = emptyMap(), // Курс -> Список групп
    val expandedCourses: Set<Int> = emptySet(),
    val expandedGroups: Set<String> = emptySet(),
    val groupSubjects: Map<String, List<String>> = emptyMap(), // Загруженные предметы для групп
    val loadingGroups: Set<String> = emptySet(), // Группы, чьи предметы сейчас грузятся
    val drafts: List<TeacherSubscription> = emptyList(), // Черновик выбранных предметов
)

class TeacherSettingsViewModel(
    private val getAllGroupListUseCase: GetAllGroupListUseCase,
    private val getGroupSubjectsUseCase: GetGroupSubjectsUseCase,
    private val preferencesRepository: TeacherPreferencesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(TeacherSettingsState())
    val state: StateFlow<TeacherSettingsState> = _state

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // Загружаем все группы
            val allGroups = getAllGroupListUseCase()
            // Группируем по первому символу (100 -> 1 курс, 200 -> 2 курс)
            val coursesMap = allGroups.groupBy { it.name.firstOrNull()?.digitToIntOrNull() ?: 0 }
                .filterKeys { it > 0 } // Убираем странные названия, если есть

            // Загружаем текущие сохраненные настройки
            val currentSubs = preferencesRepository.getSubscriptions()

            _state.update { it.copy(courses = coursesMap, drafts = currentSubs) }
        }
    }

    fun toggleCourse(course: Int) {
        _state.update { current ->
            val newExpanded = if (current.expandedCourses.contains(course)) {
                current.expandedCourses - course
            } else {
                current.expandedCourses + course
            }
            current.copy(expandedCourses = newExpanded)
        }
    }

    fun toggleGroup(groupName: String) {
        val current = _state.value
        val isExpanded = current.expandedGroups.contains(groupName)

        if (isExpanded) {
            _state.update { it.copy(expandedGroups = it.expandedGroups - groupName) }
        } else {
            _state.update { it.copy(expandedGroups = it.expandedGroups + groupName) }

            // Если предметы для этой группы еще не загружены, грузим их
            if (!current.groupSubjects.containsKey(groupName) && !current.loadingGroups.contains(
                    groupName
                )
            ) {
                loadSubjectsForGroup(groupName)
            }
        }
    }

    private fun loadSubjectsForGroup(groupName: String) {
        _state.update { it.copy(loadingGroups = it.loadingGroups + groupName) }
        viewModelScope.launch {
            val subjects = getGroupSubjectsUseCase(groupName)
            _state.update {
                it.copy(
                    groupSubjects = it.groupSubjects + (groupName to subjects),
                    loadingGroups = it.loadingGroups - groupName
                )
            }
        }
    }

    fun toggleSubjectSelection(groupName: String, subjectName: String, isSelected: Boolean) {
        _state.update { current ->
            val newDrafts = current.drafts.toMutableList()
            if (isSelected) {
                // Добавляем по умолчанию "Для обеих подгрупп" (ALL)
                if (newDrafts.none { it.groupName == groupName && it.subjectName == subjectName }) {
                    newDrafts.add(TeacherSubscription(groupName, subjectName, Subgroup.ALL))
                }
            } else {
                newDrafts.removeAll { it.groupName == groupName && it.subjectName == subjectName }
            }
            current.copy(drafts = newDrafts)
        }
    }

    fun updateSubgroup(groupName: String, subjectName: String, subgroup: Subgroup) {
        _state.update { current ->
            val newDrafts = current.drafts.map {
                if (it.groupName == groupName && it.subjectName == subjectName) {
                    it.copy(subgroup = subgroup)
                } else it
            }
            current.copy(drafts = newDrafts)
        }
    }

    fun saveSettings(onComplete: () -> Unit) {
        viewModelScope.launch {
            preferencesRepository.saveSubscriptions(_state.value.drafts)
            onComplete()
        }
    }
}