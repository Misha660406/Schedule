package com.example.schedule.feature.schedule.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedule.feature.schedule.R
import com.example.schedule.shared.schedule.domain.entity.Lesson
import com.example.schedule.shared.schedule.domain.entity.Schedule

@Composable
fun ScheduleDay(schedule: Schedule) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (schedule.lessons.isEmpty()) {
            Text(
                text = stringResource(R.string.feature_schedule_no_lessons),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn {
                items(schedule.lessons) { lesson ->
                    LessonItem(lesson)
                }
            }
        }
    }
}

@Composable
private fun LessonItem(lesson: Lesson) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${lesson.position}. ${lesson.name}",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp
            )
            Text(
                text = stringResource(R.string.feature_schedule_room, lesson.room),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
            Text(
                text = remember(lesson.position) {
                    getLessonTime(lesson.position)
                },
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
        }
    }
}

private fun getLessonTime(position: Int) = listOf(
    "08:30 - 10:00", "10:15 - 11:45", "12:15 - 13:45",
    "14:05 - 15:35", "15:45 - 17:15", "17:25 - 18:55"
).getOrElse(position - 1) { "Нет времени" }