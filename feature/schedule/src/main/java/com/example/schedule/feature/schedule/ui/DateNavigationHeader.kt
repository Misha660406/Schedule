package com.example.schedule.feature.schedule.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Header(date: LocalDate, groupName: String, onGroupSelectionClick: () -> Unit) {
    val dateOfWeekFormatter = remember {
        DateTimeFormatter.ofPattern("EEEE")
    }
    val formatter = remember {
        DateTimeFormatter.ofPattern("dd MMMM yyyy")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = remember(date) {
                date.format(dateOfWeekFormatter)
                    .replaceFirstChar { it.uppercaseChar() }
            },
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp
        )
        Text(
            text = remember(date) { date.format(formatter) },
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp
        )
        Text(
            text = groupName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onGroupSelectionClick() }
        )
    }
}