package com.example.schedule.feature.schedule.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.schedule.feature.schedule.R
import com.example.schedule.feature.schedule.presentation.ScheduleState
import com.example.schedule.shared.schedule.domain.entity.Lesson
import com.example.schedule.shared.ui.ui.theme.LessonCard
import com.example.schedule.shared.ui.ui.theme.ScheduleTheme

@Composable
fun ScheduleDay(scheduleState: ScheduleState) {
    AnimatedContent(
        targetState = scheduleState,
        contentKey = { it::class.java },
        modifier = Modifier.fillMaxSize()
    ) { currentState ->
        when (currentState) {
            is ScheduleState.ReadyToLoad, is ScheduleState.Loading -> LoadingContent()
            is ScheduleState.Loaded -> LoadedContent(currentState)
        }
    }
}

@Composable
private fun LoadedContent(scheduleState: ScheduleState.Loaded) {
    if (scheduleState.lessons.isEmpty()) {
        NoLesson()
    } else {
        LazyColumn {
            items(scheduleState.lessons) { lesson ->
                LessonItem(lesson)
            }
        }
    }
}

@Composable
private fun NoLesson() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(
            painter = painterResource(ScheduleTheme.colors.imageNoSchedule),
            contentDescription = stringResource(R.string.feature_schedule_no_lessons),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.feature_schedule_no_lessons),
            style = ScheduleTheme.typography.bodyMain,
            color = ScheduleTheme.colors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LessonItem(lesson: Lesson) {
    val (startTime, endTime) = remember(lesson.position) { getLessonTime(lesson.position) }
    LessonCard(
        lessonPosition = lesson.position.toString(),
        lessonName = lesson.name,
        lessonRoom = stringResource(R.string.feature_schedule_room, lesson.room),
        startTimeLesson = startTime,
        endTimeLesson = endTime,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

private fun getLessonTime(position: Int): Pair<String, String> = listOf(
    "08:30" to "10:00",
    "10:15" to "11:45",
    "12:15" to "13:45",
    "14:05" to "15:35",
    "15:45" to "17:15",
    "17:25" to "18:55"
).getOrElse(position - 1) { "Нет времени" to "Нет времени" }