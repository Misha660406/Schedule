package com.example.schedule.shared.schedule.data.repository

import com.example.schedule.shared.schedule.domain.entity.Lesson
import com.example.schedule.shared.schedule.domain.entity.Schedule
import com.example.schedule.shared.schedule.domain.repository.ScheduleRepository
import java.time.DayOfWeek
import java.time.LocalDate

class ScheduleRepositoryImpl : ScheduleRepository {

    override suspend fun getByDate(groupId: Long, date: LocalDate): Schedule =
        Schedule(
            date = date,
            lessons = when (groupId) {
                100L -> getLessonsForGroup100(date.dayOfWeek)
                200L -> getLessonsForGroup200(date.dayOfWeek)
                300L -> getLessonsForGroup300(date.dayOfWeek)
                else -> emptyList()
            }
        )

    private fun getLessonsForGroup100(dayOfWeek: DayOfWeek): List<Lesson> =
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> listOf(
                Lesson(
                    name = "Литература",
                    room = "208",
                    teacher = "Крыгина Роза Михайловна",
                    position = 1
                ),
                Lesson(
                    name = "Россия - моя история",
                    room = "302",
                    teacher = "Ермолович Борис Александрович",
                    position = 2
                ),
                Lesson(
                    name = "Химия",
                    room = "3м",
                    teacher = "Дмитриенко Константин Евгеньевич",
                    position = 3
                )
            )

            DayOfWeek.TUESDAY -> listOf(
                Lesson(
                    name = "ОБ и защиты родины",
                    room = "55м",
                    teacher = "Копцев Владимир Михайлович",
                    position = 1
                ),
                Lesson(
                    name = "Физическая культура",
                    room = "с/з",
                    teacher = "Сайлер Евгений Викторович",
                    position = 2
                )
            )

            DayOfWeek.WEDNESDAY -> listOf(
                Lesson(
                    name = "История",
                    room = "14м",
                    teacher = "Билалов Алик Мирхатимович",
                    position = 1
                ),
                Lesson(
                    name = "Биология",
                    room = "206",
                    teacher = "Лапицкая Татьяна Владимировна",
                    position = 2
                ),
                Lesson(
                    name = "География",
                    room = "206",
                    teacher = "Лапицкая Татьяна Владимировна",
                    position = 3
                ),
                Lesson(
                    name = "Математика",
                    room = "4м",
                    teacher = "Припускова Ирина Георгиевна",
                    position = 4
                )
            )

            DayOfWeek.THURSDAY -> listOf(
                Lesson(
                    name = "Русский язык",
                    room = "208",
                    teacher = "Крыгина Роза Михайловна",
                    position = 1
                ),
                Lesson(
                    name = "Основы проектной деятельности",
                    room = "304",
                    teacher = "Ковалёва Юлия Алексеевна",
                    position = 2
                ),
                Lesson(
                    name = "Литература",
                    room = "208",
                    teacher = "Крыгина Роза Михайловна",
                    position = 3
                )
            )

            DayOfWeek.FRIDAY -> listOf(
                Lesson(
                    name = "Химия",
                    room = "3м",
                    teacher = "Дмитриенко Константин Евгеньевич",
                    position = 1
                ),
                Lesson(
                    name = "Физика",
                    room = "111",
                    teacher = "Сыздыкова Зиля Игоревна",
                    position = 2
                ),
                Lesson(
                    name = "Математика",
                    room = "4м",
                    teacher = "Припускова Ирина Георгиевна",
                    position = 3
                ),
                Lesson(
                    name = "Математика",
                    room = "4м",
                    teacher = "Припускова Ирина Георгиевна",
                    position = 4
                )
            )

            DayOfWeek.SATURDAY -> listOf(
                Lesson(
                    name = "Обществознание",
                    room = "14м",
                    teacher = "Билалов Алик Мирхатимович",
                    position = 1
                ),
                Lesson(
                    name = "Физическая культура",
                    room = "с/з",
                    teacher = "Сайлер Евгений Викторович",
                    position = 2
                ),
                Lesson(
                    name = "Избранные вопросы математики",
                    room = "4м",
                    teacher = "Припускова Ирина Георгиевна",
                    position = 3
                )
            )

            DayOfWeek.SUNDAY -> emptyList()
        }

    private fun getLessonsForGroup200(dayOfWeek: DayOfWeek): List<Lesson> =
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> listOf(
                Lesson(
                    name = "Ботаника с основами физиологии растений",
                    room = "206",
                    teacher = "Лапицкая Татьяна Владимировна",
                    position = 1
                ),
                Lesson(
                    name = "Флористика",
                    room = "8м",
                    teacher = "Солдатова Юлия Николаевна",
                    position = 2
                ),
                Lesson(
                    name = "Декоративная дендрология",
                    room = "8м",
                    teacher = "Солдатова Юлия Николаевна",
                    position = 3
                )
            )

            DayOfWeek.TUESDAY -> listOf(
                Lesson(
                    name = "История России",
                    room = "302",
                    teacher = "Ермолович Борис Александрович",
                    position = 1
                ),
                Lesson(
                    name = "Физическая культура",
                    room = "спорт.зал",
                    teacher = "Заворин Александр Александрович",
                    position = 2
                ),
                Lesson(
                    name = "Ин.Язык",
                    room = "18м",
                    teacher = "Ануфриева Юлия Юрьевна",
                    position = 3
                ),
                Lesson(
                    name = "Ин.Язык",
                    room = "301",
                    teacher = "Балдина Ирина Петровна",
                    position = 3
                )
            )

            DayOfWeek.WEDNESDAY -> listOf(
                Lesson(
                    name = "Декоративное растениеводство и питомниководство",
                    room = "8м",
                    teacher = "Солдатова Юлия Николаевна",
                    position = 1
                ),
                Lesson(
                    name = "МДК.03.01Технология выполнения работ по профессии 17531 Рабочий зеленого хозяйства",
                    room = "8м",
                    teacher = "Солдатова Юлия Николаевна",
                    position = 2
                ),
                Lesson(
                    name = "Основы бережливого производства",
                    room = "305",
                    teacher = "Зильбернагель Яна Геннадьевна",
                    position = 3
                )
            )

            DayOfWeek.THURSDAY -> listOf(
                Lesson(
                    name = "Декоративная дендрология",
                    room = "8м",
                    teacher = "Солдатова Юлия Николаевна",
                    position = 1
                ),
                Lesson(
                    name = "Безопасность жизнедеятельности",
                    room = "55м",
                    teacher = "Копцев Владимир Михайлович",
                    position = 2
                )
            )

            DayOfWeek.FRIDAY -> listOf(
                Lesson(
                    name = "Геодезия",
                    room = "209",
                    teacher = "Ермакова Наталья Владимировна",
                    position = 1
                ),
                Lesson(
                    name = "История садово-паркового искусства",
                    room = "8м",
                    teacher = "Солдатова Юлия Николаевна",
                    position = 2
                )
            )

            DayOfWeek.SATURDAY -> listOf(
                Lesson(
                    name = "Физическая культура",
                    room = "спорт.зал",
                    teacher = "Заворин Александр Александрович",
                    position = 1
                ),
                Lesson(
                    name = "История России",
                    room = "302",
                    teacher = "Ермолович Борис Александрович",
                    position = 2
                ),
                Lesson(
                    name = "Основы финансовой грамотности",
                    room = "6",
                    teacher = "Черных Зоя Владимировна",
                    position = 3
                )
            )

            DayOfWeek.SUNDAY -> emptyList()
        }

    private fun getLessonsForGroup300(dayOfWeek: DayOfWeek): List<Lesson> =
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> listOf(
                Lesson(
                    name = "Цветочно-декоративные растения и дендрология",
                    room = "15м",
                    teacher = "Пономарёва Татьяна Владимировна",
                    position = 1
                ),
                Lesson(
                    name = "ПОСПС",
                    room = "15м",
                    teacher = "Пономарёва Татьяна Владимировна",
                    position = 2
                )
            )

            DayOfWeek.TUESDAY -> listOf(
                Lesson(
                    name = "Безопасность жизнедеятельности",
                    room = "55м",
                    teacher = "Копцев Владимир Михайлович",
                    position = 1
                )
            )

            DayOfWeek.WEDNESDAY -> listOf(
                Lesson(
                    name = "Экологические основы природопользования",
                    room = "3м",
                    teacher = "Дмитриенко Константин Евгеньевич",
                    position = 1
                ),
                Lesson(
                    name = "Физическая культура",
                    room = "спорт.зал",
                    teacher = "Заворин Александр Александрович",
                    position = 2
                ),
                Lesson(
                    name = "Охрана труда",
                    room = "306",
                    teacher = "Хадеева Наталья Александровна",
                    position = 3
                )
            )

            DayOfWeek.THURSDAY -> listOf(
                Lesson(
                    name = "Озеленение интерьеров",
                    room = "15м",
                    teacher = "Пономарёва Татьяна Владимировна",
                    position = 1
                ),
                Lesson(
                    name = "Информационные технологии в профессиональной деятельности",
                    room = "4",
                    teacher = "Сычев Дмитрий Андреевич",
                    position = 2
                ),
                Lesson(
                    name = "Фитодизайн",
                    room = "15м",
                    teacher = "Снегурова Людмила Константиновна",
                    position = 3
                )
            )

            DayOfWeek.FRIDAY -> listOf(
                Lesson(
                    name = "Ин.Язык",
                    room = "5м",
                    teacher = "Панченко Елена Николаевна",
                    position = 1
                ),
                Lesson(
                    name = "Ин.Язык",
                    room = "301",
                    teacher = "Балдина Ирина Петровна",
                    position = 1
                ),
                Lesson(
                    name = "Цветочно-декоративные растения и дендрология",
                    room = "15м",
                    teacher = "Пономарёва Татьяна Владимировна",
                    position = 2
                )
            )

            DayOfWeek.SATURDAY -> listOf(
                Lesson(
                    name = "Физическая культура",
                    room = "спорт.зал",
                    teacher = "Заворин Александр Александрович",
                    position = 1
                ),
                Lesson(
                    name = "Цветочно-декоративные растения и дендрология",
                    room = "15м",
                    teacher = "Пономарёва Татьяна Владимировна",
                    position = 2
                )
            )

            DayOfWeek.SUNDAY -> emptyList()
        }
}