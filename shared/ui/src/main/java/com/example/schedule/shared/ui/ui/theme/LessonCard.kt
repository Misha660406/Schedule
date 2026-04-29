package com.example.schedule.shared.ui.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LessonCard(
    lessonPosition: String,
    lessonName: String,
    lessonRoom: String,
    startTimeLesson: String,
    endTimeLesson: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.outlinedCardColors(containerColor = ScheduleTheme.colors.surface),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = lessonPosition,
                style = ScheduleTheme.typography.h2,
                color = ScheduleTheme.colors.textSecondary,
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lessonName,
                    style = ScheduleTheme.typography.h3,
                    color = ScheduleTheme.colors.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lessonRoom,
                    style = ScheduleTheme.typography.bodyTertiary,
                    color = ScheduleTheme.colors.textPrimary
                )
            }

            Spacer(modifier = Modifier.padding(end = 4.dp))

            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                color = ScheduleTheme.colors.textSecondary
            )

            Column(
                modifier = Modifier.padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = startTimeLesson,
                    style = ScheduleTheme.typography.bodyMain,
                    color = ScheduleTheme.colors.textPrimary
                )
                Text(
                    text = endTimeLesson,
                    style = ScheduleTheme.typography.bodyMain,
                    color = ScheduleTheme.colors.textPrimary
                )
            }
        }
    }
}