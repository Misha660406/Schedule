package com.example.schedule.feature.schedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.feature.schedule.presentation.ScheduleState
import com.example.schedule.feature.schedule.presentation.ScheduleViewModel
import com.example.schedule.libs.navigation.Screen
import com.example.schedule.shared.schedule.domain.entity.Lesson
import org.koin.java.KoinJavaComponent.inject
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter

class ScheduleScreen : Screen {

    private val viewModel: ScheduleViewModel by inject(ScheduleViewModel::class.java)

    @Composable
    override fun Render() {
        val state by viewModel.state.collectAsState()
        val pagerState = rememberPagerState(
            initialPage = 1000,
            pageCount = { 2000 }
        )

        var previousPage by remember { mutableIntStateOf(1000) }

        LaunchedEffect(Unit) {
            viewModel.init()
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { currentPage ->
                if (currentPage != previousPage) {
                    when {
                        currentPage > previousPage -> viewModel.loadNextDate()
                        currentPage < previousPage -> viewModel.loadPreviousDate()
                    }
                    previousPage = currentPage
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            DateNavigationHeader(state, viewModel)

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) {
                ScheduleContent(state)
            }
        }
    }

    @Composable
    private fun DateNavigationHeader(
        state: ScheduleState,
        viewModel: ScheduleViewModel
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.loadPreviousDate() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Пред. день")
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state is ScheduleState.ContentWithSelected) {
                    Text(
                        text = getDayOfWeekText(state.date.dayOfWeek),
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp
                    )
                    Text(
                        text = state.date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp
                    )
                }
            }

            IconButton(onClick = { viewModel.loadNextDate() }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "След. день")
            }
        }
    }

    @Composable
    private fun ScheduleContent(state: ScheduleState) {
        when (state) {
            is ScheduleState.ContentWithSelected -> {
                ScheduleTable(state)
            }

            is ScheduleState.Initial -> {
                Text(text = "Загружаем расписание...")
            }

            is ScheduleState.Loading -> {
                Text(text = "Загрузка...")
            }

            is ScheduleState.Content -> {
                Text(text = "Расписание для группы 100 (нет выбранной группы)")
            }
        }
    }

    @Composable
    private fun ScheduleTable(state: ScheduleState.ContentWithSelected) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            ScheduleTableHeader()

            val maxPairs = if (state.date.dayOfWeek == DayOfWeek.THURSDAY) 5 else 6
            for (i in 1..maxPairs) {
                val lesson = state.schedule.find { it.position == i }
                LessonItem(lesson, state.date.dayOfWeek, i)
            }
        }
    }

    @Composable
    private fun ScheduleTableHeader() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = 4.dp)
        ) {
            Text("№", modifier = Modifier.weight(0.5f), style = MaterialTheme.typography.bodyMedium)
            Text(
                "Предмет",
                modifier = Modifier.weight(2f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Каб.",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Время",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    @Composable
    private fun LessonItem(lesson: Lesson?, dayOfWeek: DayOfWeek, position: Int) {
        val timeString = getLessonTime(dayOfWeek, position)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("$position", modifier = Modifier.weight(0.5f))
            Text(lesson?.name ?: "", modifier = Modifier.weight(2f))
            Text(lesson?.room ?: "", modifier = Modifier.weight(1f))
            Text(timeString, modifier = Modifier.weight(1f))
        }
    }

    private fun getDayOfWeekText(dayOfWeek: DayOfWeek): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> "Понедельник"
            DayOfWeek.TUESDAY -> "Вторник"
            DayOfWeek.WEDNESDAY -> "Среда"
            DayOfWeek.THURSDAY -> "Четверг"
            DayOfWeek.FRIDAY -> "Пятница"
            DayOfWeek.SATURDAY -> "Суббота"
            else -> ""
        }
    }

    private fun getLessonTime(dayOfWeek: DayOfWeek, position: Int): String {
        val timeSlots = when (dayOfWeek) {
            DayOfWeek.MONDAY -> arrayOf(
                "09:25 - 10:55", "11:05 - 12:35", "13:00 - 14:30",
                "14:40 - 16:10", "16:20 - 17:50", "18:00 - 19:30"
            )

            DayOfWeek.THURSDAY -> arrayOf(
                "10:15 - 11:45", "12:15 - 13:45", "14:05 - 15:35",
                "15:45 - 17:15", "17:25 - 18:55"
            )

            else -> arrayOf(
                "08:30 - 10:00", "10:15 - 11:45", "12:15 - 13:45",
                "14:05 - 15:35", "15:45 - 17:15", "17:25 - 18:55"
            )
        }
        return if (position in 1..timeSlots.size) timeSlots[position - 1] else "Нет времени"
    }
}