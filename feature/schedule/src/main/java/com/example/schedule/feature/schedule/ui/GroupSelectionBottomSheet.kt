package com.example.schedule.feature.schedule.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.schedule.feature.schedule.R
import com.example.schedule.feature.schedule.presentation.SelectedGroupState
import com.example.schedule.feature.schedule.presentation.State
import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.ui.ui.theme.ScheduleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupSelectorBottomSheet(
    state: State.Content,
    onGroupSelected: (Group) -> Unit,
    onCloseGroupSelector: () -> Unit
) {
    if (state.selectedGroupState == SelectedGroupState.SELECTING) {
        ModalBottomSheet(
            containerColor = ScheduleTheme.colors.background,
            onDismissRequest = { onCloseGroupSelector() },
        ) {
            GroupSelectionBottomSheetContent(
                groups = state.selectedGroupList,
                onGroupSelected = onGroupSelected
            )
        }
    }
}

@Composable
private fun GroupSelectionBottomSheetContent(
    groups: List<Group>,
    onGroupSelected: (Group) -> Unit,
) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = stringResource(R.string.feature_schedule_select_group),
            style = ScheduleTheme.typography.h2,
            color = ScheduleTheme.colors.textPrimary,
            modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        )
        LazyColumn {
            items(groups) { group ->
                Text(
                    text = group.name,
                    style = ScheduleTheme.typography.bodyMain,
                    color = ScheduleTheme.colors.textPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGroupSelected(group) }
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}