package com.example.schedule.shared.schedule.data.repository

import com.example.schedule.shared.schedule.domain.entity.Lesson
import com.example.schedule.shared.schedule.domain.entity.Schedule
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import com.example.schedule.shared.schedule.domain.repository.TeacherScheduleRepository
import java.time.LocalDate

class TeacherScheduleRepositoryImpl(
    private val scheduleRepository: ScheduleRepository
) : TeacherScheduleRepository {

    private data class Subscription(val groupId: Long, val subjectName: String)

    private val teacherSubscriptions = listOf(
        Subscription(groupId = 100L, subjectName = "Математика"),
        Subscription(groupId = 100L, subjectName = "Химия"),
        Subscription(groupId = 200L, subjectName = "Геодезия"),
        Subscription(groupId = 300L, subjectName = "Ин.Язык")
    )

    private val allGroupIds = listOf(100L, 200L, 300L)

    override suspend fun getTeacherSchedule(date: LocalDate): Schedule {
        val teacherLessons = mutableListOf<Lesson>()

        for (groupId in allGroupIds) {
            val schedule = scheduleRepository.getByDate(groupId, date)
            val groupLessons = schedule.lessons
            val subscriptionsForGroup = teacherSubscriptions.filter { it.groupId == groupId }

            for (lesson in groupLessons) {
                val isSubjectRelevant =
                    subscriptionsForGroup.any { sub -> sub.subjectName == lesson.name }

                if (isSubjectRelevant) {
                    teacherLessons.add(lesson.copy(name = "${lesson.name} (Гр. $groupId)"))
                }
            }
        }

        return Schedule(date = date, lessons = teacherLessons.sortedBy { it.position })
    }
}