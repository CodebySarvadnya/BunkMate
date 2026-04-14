package com.example.bunkmate.logic

import java.util.Calendar

data class DaySchedule(
    val day: String,
    val lectures: Int
)

object TimetableProvider {
    // Simple in-memory timetable
    private val schedule = mapOf(
        Calendar.MONDAY to DaySchedule("Monday", 5),
        Calendar.TUESDAY to DaySchedule("Tuesday", 4),
        Calendar.WEDNESDAY to DaySchedule("Wednesday", 6),
        Calendar.THURSDAY to DaySchedule("Thursday", 4),
        Calendar.FRIDAY to DaySchedule("Friday", 5),
        Calendar.SATURDAY to DaySchedule("Saturday", 2),
        Calendar.SUNDAY to DaySchedule("Sunday", 0)
    )

    /**
     * Returns the number of lectures for a given date in "yyyy-MM-dd" format
     */
    fun getLecturesForDate(dateMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateMillis
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return schedule[dayOfWeek]?.lectures ?: 0
    }
}