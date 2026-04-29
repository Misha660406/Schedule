package com.example.schedule.feature.schedule.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.schedule.feature.schedule.presentation.TeacherSettingsViewModel
import com.example.schedule.shared.schedule.domain.entity.Subgroup
import com.example.schedule.shared.ui.ui.theme.ScheduleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherSettingsBottomSheet(
    viewModel: TeacherSettingsViewModel,
    onDismiss: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ScheduleTheme.colors.background,
        modifier = Modifier.fillMaxHeight(0.9f) // Занимает 90% экрана
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Настройки расписания",
                style = ScheduleTheme.typography.h1,
                color = ScheduleTheme.colors.textPrimary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                state.courses.toSortedMap().forEach { (course, groups) ->
                    item {
                        CourseItem(
                            course = course,
                            isExpanded = state.expandedCourses.contains(course),
                            onClick = { viewModel.toggleCourse(course) }
                        )
                    }

                    if (state.expandedCourses.contains(course)) {
                        groups.forEach { group ->
                            item {
                                GroupItem(
                                    groupName = group.name,
                                    isExpanded = state.expandedGroups.contains(group.name),
                                    isLoading = state.loadingGroups.contains(group.name),
                                    onClick = { viewModel.toggleGroup(group.name) }
                                )
                            }

                            if (state.expandedGroups.contains(group.name)) {
                                val subjects = state.groupSubjects[group.name] ?: emptyList()
                                items(subjects.size) { index ->
                                    val subject = subjects[index]
                                    val draft =
                                        state.drafts.find { it.groupName == group.name && it.subjectName == subject }

                                    SubjectItem(
                                        subjectName = subject,
                                        isChecked = draft != null,
                                        selectedSubgroup = draft?.subgroup ?: Subgroup.ALL,
                                        onCheckChange = { isChecked ->
                                            viewModel.toggleSubjectSelection(
                                                group.name,
                                                subject,
                                                isChecked
                                            )
                                        },
                                        onSubgroupChange = { subgroup ->
                                            viewModel.updateSubgroup(group.name, subject, subgroup)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Кнопка сохранения внизу
            Button(
                onClick = { viewModel.saveSettings(onComplete = onDismiss) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ScheduleTheme.colors.accent)
            ) {
                Text(text = "Сохранить", color = ScheduleTheme.colors.textPrimary)
            }
        }
    }
}

@Composable
private fun CourseItem(course: Int, isExpanded: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$course Курс",
            style = ScheduleTheme.typography.h2.copy(fontWeight = FontWeight.Bold),
            color = ScheduleTheme.colors.textPrimary,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(if (isExpanded) ScheduleTheme.colors.imageArrowUp else ScheduleTheme.colors.imageArrowDown),
            contentDescription = null,
            tint = ScheduleTheme.colors.textPrimary
        )
    }
}

@Composable
private fun GroupItem(
    groupName: String,
    isExpanded: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(ScheduleTheme.colors.surface)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Группа $groupName",
            style = ScheduleTheme.typography.h3,
            color = ScheduleTheme.colors.textPrimary,
            modifier = Modifier.weight(1f)
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = ScheduleTheme.colors.accent
            )
        } else {
            Icon(
                painter = painterResource(if (isExpanded) ScheduleTheme.colors.imageArrowUp else ScheduleTheme.colors.imageArrowDown),
                contentDescription = null,
                tint = ScheduleTheme.colors.textSecondary
            )
        }
    }
}

private fun isSubjectWithSubgroups(subjectName: String): Boolean {
    val nameLower = subjectName.lowercase()
    return nameLower.contains("ин.язык") ||
            nameLower.contains("индив.проект") ||
            nameLower.contains("живопись с осн.цвет") ||
            nameLower.contains("рисунок с осн.персп")
}

@Composable
private fun SubjectItem(
    subjectName: String,
    isChecked: Boolean,
    selectedSubgroup: Subgroup,
    onCheckChange: (Boolean) -> Unit,
    onSubgroupChange: (Subgroup) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckChange,
                colors = CheckboxDefaults.colors(checkedColor = ScheduleTheme.colors.accent)
            )
            Text(text = subjectName, color = ScheduleTheme.colors.textPrimary)
        }

        // ПОКАЗЫВАЕМ ВЫБОР ПОДГРУПП ТОЛЬКО ДЛЯ НУЖНЫХ ПРЕДМЕТОВ!
        val showSubgroups = isChecked && isSubjectWithSubgroups(subjectName)

        AnimatedVisibility(visible = showSubgroups) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SubgroupOption("Все", Subgroup.ALL, selectedSubgroup, onSubgroupChange)
                SubgroupOption("1 (а)", Subgroup.FIRST, selectedSubgroup, onSubgroupChange)
                SubgroupOption("2 (н)", Subgroup.SECOND, selectedSubgroup, onSubgroupChange)
            }
        }
    }
}

@Composable
private fun SubgroupOption(
    title: String,
    value: Subgroup,
    selectedValue: Subgroup,
    onClick: (Subgroup) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick(value) }) {
        RadioButton(
            selected = selectedValue == value,
            onClick = { onClick(value) },
            colors = RadioButtonDefaults.colors(selectedColor = ScheduleTheme.colors.accent)
        )
        Text(
            text = title,
            style = ScheduleTheme.typography.bodyTertiary,
            color = ScheduleTheme.colors.textPrimary
        )
    }
}
