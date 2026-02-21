package com.example.schedule.feature.schedule.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.schedule.feature.schedule.R
import com.example.schedule.feature.schedule.presentation.SelectedGroupState
import com.example.schedule.feature.schedule.presentation.State
import com.example.schedule.shared.group.domain.entity.Group
import com.example.schedule.shared.ui.ui.theme.GroupCard
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
                selectedGroup = state.selectedGroup,
                onGroupSelected = onGroupSelected
            )
        }
    }
}

@Composable
private fun GroupSelectionBottomSheetContent(
    groups: List<Group>,
    selectedGroup: Group?,
    onGroupSelected: (Group) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.feature_schedule_group),
            style = ScheduleTheme.typography.h2,
            color = ScheduleTheme.colors.textPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        GroupCard(
            groups = groups,
            selectedGroups = if (selectedGroup != null) listOf(selectedGroup) else emptyList(),
            onGroupClick = onGroupSelected
        )
    }
}