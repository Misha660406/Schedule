package com.example.schedule.shared.schedule.data.repository

import com.example.schedule.shared.schedule.domain.entity.Lesson
import com.example.schedule.shared.schedule.domain.entity.Schedule
import com.example.schedule.shared.schedule.domain.entity.Subgroup
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import com.example.schedule.shared.schedule.domain.repository.TeacherPreferencesRepository
import com.example.schedule.shared.schedule.domain.repository.TeacherScheduleRepository
import java.time.LocalDate

class TeacherScheduleRepositoryImpl(
    private val scheduleRepository: ScheduleRepository,
    private val preferencesRepository: TeacherPreferencesRepository,
) : TeacherScheduleRepository {

    // Временный класс для хранения найденных совпадений перед группировкой
    private data class Match(
        val position: Int,
        val originalName: String,
        val room: String,
        val groupName: String,
        val baseSubject: String,
    )

    override suspend fun getTeacherSchedule(date: LocalDate): Schedule {
        // 1. Читаем JSON с настройками преподавателя (из памяти телефона)
        val subscriptions = preferencesRepository.getSubscriptions()
        val activeGroups = subscriptions.map { it.groupName }.distinct()

        val allMatches = mutableListOf<Match>()

        // 2. Ищем все пары преподавателя по группам
        for (groupName in activeGroups) {
            val schedule = scheduleRepository.getByDate(groupName, date)
            val subsForGroup = subscriptions.filter { it.groupName == groupName }

            for (lesson in schedule.lessons) {
                val cleanDailyName = getCleanSubjectName(lesson.name)
                val dailySubgroup = extractSubgroup(lesson.name)

                // Проверяем, ведет ли препод эту пару у этой подгруппы
                val matchedSub = subsForGroup.find { sub ->
                    val nameMatches = sub.subjectName.equals(cleanDailyName, ignoreCase = true)
                    val subgroupMatches = (dailySubgroup == Subgroup.ALL) ||
                            (sub.subgroup == Subgroup.ALL) ||
                            (sub.subgroup == dailySubgroup)
                    nameMatches && subgroupMatches
                }

                if (matchedSub != null) {
                    allMatches.add(
                        Match(
                            position = lesson.position,
                            originalName = lesson.name,
                            room = lesson.room,
                            groupName = groupName,
                            baseSubject = matchedSub.subjectName // То, что препод отметил галочкой
                        )
                    )
                }
            }
        }

        // 3. МАГИЯ СОВМЕЩЕНКИ (Группируем по позиции и базовому предмету)
        val teacherLessons = allMatches
            .groupBy { it.position to it.baseSubject }
            .map { (key, matchesList) ->
                val position = key.first

                // Берем оригинальное имя из первой пары (чтобы сохранить приписки вроде "УМ", если они есть),
                // но удаляем маркеры подгрупп (1) или (2)
                val displaySubject = matchesList.first().originalName
                    .replace(Regex("\\s*\\([12анАН]\\)"), "")
                    .trim()

                // Кабинет берем из первой пары (обычно при совмещенке кабинет один на всех)
                val room = matchesList.first().room

                // Собираем группы через запятую и сортируем по алфавиту/цифрам
                val groupsString = matchesList.map { it.groupName }
                    .distinct()
                    .sorted()
                    .joinToString(", ")

                Lesson(
                    position = position,
                    name = "$displaySubject (Гр. $groupsString)",
                    room = room,
                    teacher = ""
                )
            }
            .sortedBy { it.position } // Сортируем пары по времени (1, 2, 3...)

        return Schedule(date = date, lessons = teacherLessons)
    }

    private fun extractSubgroup(lessonName: String): Subgroup {
        val nameLower = lessonName.lowercase()
        return when {
            nameLower.contains("(1)") || nameLower.contains("(а)") -> Subgroup.FIRST
            nameLower.contains("(2)") || nameLower.contains("(н)") -> Subgroup.SECOND
            else -> Subgroup.ALL
        }
    }

    private fun getCleanSubjectName(lessonName: String): String {
        return lessonName
            .replace(Regex("\\s*\\([12анАН]\\)"), "")
            .trim()
    }
}