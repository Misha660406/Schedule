package com.example.schedule.feature.schedule.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.schedule.feature.schedule.presentation.ScheduleViewModel
import com.example.schedule.feature.schedule.presentation.TeacherScheduleViewModel
import com.example.schedule.feature.schedule.presentation.TeacherSettingsViewModel
import com.example.schedule.libs.navigation.Screen
import org.koin.java.KoinJavaComponent.inject

class MainTestScreen : Screen {

    // Инжектим обе ViewModel (и для студента, и для учителя)
    private val studentViewModel: ScheduleViewModel by inject(ScheduleViewModel::class.java)
    private val teacherViewModel: TeacherScheduleViewModel by inject(TeacherScheduleViewModel::class.java)
    private val teacherSettingsViewModel: TeacherSettingsViewModel by inject(
        TeacherSettingsViewModel::class.java
    )

    @Composable
    override fun Render() {
        // Переключатель состояния (false = Студент, true = Учитель)
        var isTeacherMode by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {

            // 1. ПОКАЗЫВАЕМ КОНТЕНТ В ЗАВИСИМОСТИ ОТ РЕЖИМА
            if (isTeacherMode) {
                TeacherContent()
            } else {
                StudentContent()
            }

            // 2. КНОПКА ПЕРЕКЛЮЧЕНИЯ (ПОВЕРХ ВСЕГО ВНИЗУ СПРАВА)
            Button(
                onClick = { isTeacherMode = !isTeacherMode },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(bottom = 50.dp) // Чуть выше, чтобы не перекрывать табы
            ) {
                Text(text = if (isTeacherMode) "Вернуться к студенту" else "Режим учителя")
            }
        }
    }

    @Composable
    private fun StudentContent() {
        val state by studentViewModel.state.collectAsState()

        LaunchedEffect(Unit) { studentViewModel.loadInitialData() }

        ScheduleContent(
            state = state,
            onSelectedScheduleIndexChangedListener = studentViewModel::updateSelectedScheduleIndex,
            onOpenGroupSelectorListener = studentViewModel::startGroupSelecting,
            onCloseGroupSelectorListener = studentViewModel::cancelGroupSelecting,
            onGroupSelectedListener = studentViewModel::selectNewGroup,
            onPreviousDayListener = studentViewModel::getPreviousDay,
            onNextDayListener = studentViewModel::getNextDay,
            onDateSelectedListener = studentViewModel::selectDate // НОВОЕ
        )
    }

    @Composable
    private fun TeacherContent() {
        val state by teacherViewModel.state.collectAsState()
        var showSettings by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) { teacherViewModel.loadInitialData() }

        if (showSettings) {
            TeacherSettingsBottomSheet(
                viewModel = teacherSettingsViewModel,
                onDismiss = {
                    showSettings = false
                    teacherViewModel.reloadCurrentSchedule()
                }
            )
        }

        ScheduleContent(
            state = state,
            onSelectedScheduleIndexChangedListener = teacherViewModel::updateSelectedScheduleIndex,
            onOpenGroupSelectorListener = { },
            onCloseGroupSelectorListener = { },
            onGroupSelectedListener = { },
            onPreviousDayListener = teacherViewModel::getPreviousDay,
            onNextDayListener = teacherViewModel::getNextDay,
            onDateSelectedListener = teacherViewModel::selectDate,
            onSettingsClickListener = { showSettings = true },
        )
    }
}