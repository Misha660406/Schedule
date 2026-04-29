package com.example.schedule.feature.schedule.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.schedule.feature.schedule.presentation.SelectedGroupState
import com.example.schedule.shared.ui.ui.theme.ScheduleTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Header(
    date: LocalDate,
    groupName: String,
    selectedGroupState: SelectedGroupState,
    onGroupSelectionClick: () -> Unit,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onDateClick: () -> Unit,
    onSettingsClick: (() -> Unit)? = null,
) {
    val dateOfWeekFormatter = remember { DateTimeFormatter.ofPattern("EEEE") }
    val formatter = remember { DateTimeFormatter.ofPattern("dd MMMM yyyy") }

    val arrowIcon = when (selectedGroupState) {
        SelectedGroupState.SELECTING -> ScheduleTheme.colors.imageArrowUp
        SelectedGroupState.SELECTED -> ScheduleTheme.colors.imageArrowDown
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Обернули в Box для правильного позиционирования
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            // Название группы по центру
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onGroupSelectionClick() }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = groupName,
                    style = ScheduleTheme.typography.h2,
                    color = ScheduleTheme.colors.textPrimary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(arrowIcon),
                    contentDescription = "Select Group"
                )
            }

            // Шестеренка с правого края
            if (onSettingsClick != null) {
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Image(
                        painter = painterResource(ScheduleTheme.colors.imageSettings),
                        contentDescription = "Settings"
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousDayClick) {
                Image(
                    painter = painterResource(ScheduleTheme.colors.imageArrowLeft),
                    contentDescription = "Previous Day"
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onDateClick() }
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = remember(date) {
                        date.format(dateOfWeekFormatter).replaceFirstChar { it.uppercaseChar() }
                    },
                    style = ScheduleTheme.typography.h3,
                    color = ScheduleTheme.colors.textPrimary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = remember(date) { date.format(formatter) },
                    style = ScheduleTheme.typography.h4,
                    color = ScheduleTheme.colors.textSecondary,
                )
            }

            IconButton(onClick = onNextDayClick) {
                Image(
                    painter = painterResource(ScheduleTheme.colors.imageArrowRight),
                    contentDescription = "Next Day"
                )
            }
        }
    }
}