package com.example.schedule.shared.schedule.data.repository

import com.example.schedule.shared.schedule.data.ScheduleApi
import com.example.schedule.shared.schedule.domain.entity.Lesson
import com.example.schedule.shared.schedule.domain.entity.Schedule
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleRepositoryImpl(
    private val api: ScheduleApi,
) : ScheduleRepository {

    // Форматер даты: API ждет дату в формате файла, например "15.04.2026"
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override suspend fun getByDate(groupId: String, date: LocalDate): Schedule {
        val dateString = date.format(dateFormatter)

        val lessons = try {
            // Делаем запрос к твоему VPS
            val response = api.getSchedule(groupName = groupId, date = dateString)

            // Превращаем LessonDto с сервера в Lesson для UI
            response.map { dto ->
                Lesson(
                    position = dto.position,
                    name = dto.name,
                    room = dto.room,
                    teacher = "" // У нас пока нет препода из парсера, оставляем пустым
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Если ошибка интернета или на этот день нет файла (404), отдаем пустоту
            emptyList()
        }

        return Schedule(
            date = date,
            lessons = lessons
        )
    }
}