package com.example.bunkmate.logic

import java.util.Locale
import kotlin.math.ceil

class AttendanceCalculator {

    /**
     * Calculates current attendance percentage.
     * Returns 0.0 if total lectures are 0.
     */
    fun calculateAttendance(attended: Int, total: Int): Double {
        if (total <= 0) return 0.0
        val percentage = (attended.toDouble() / total.toDouble()) * 100
        return roundToTwoDecimals(percentage)
    }

    /**
     * Calculates how many consecutive lectures are needed to reach a target percentage.
     * Returns 0 if already at or above target.
     */
    fun lecturesNeededForTarget(attended: Int, total: Int, targetPercent: Double): Int {
        if (targetPercent <= 0) return 0
        if (targetPercent >= 100) return if (attended == total && total > 0) 0 else -1 // -1 signifies impossible if target is 100% and one was missed

        val currentPercent = if (total > 0) (attended.toDouble() / total) * 100 else 0.0

        if (currentPercent >= targetPercent) return 0

        val numerator = (targetPercent * total) - (100 * attended)
        val denominator = 100 - targetPercent
        val needed = ceil(numerator / denominator).toInt()

        return if (needed > 0) needed else 0
    }

    fun lecturesCanBunk(attended: Int, total: Int, targetPercent: Double): Int {
        if (total <= 0 || targetPercent <= 0) return 0

        val maxTotalPossible = (100 * attended) / targetPercent
        val canSkip = (maxTotalPossible - total).toInt()

        return if (canSkip > 0) canSkip else 0
    }

    /*Attendance after attending and missing a set number of future lectures.*/
    fun futureAttendance(attended: Int, total: Int, willAttend: Int, willMiss: Int): Double {
        val newAttended = attended + willAttend
        val newTotal = total + willAttend + willMiss

        return calculateAttendance(newAttended, newTotal)
    }

    private fun roundToTwoDecimals(value: Double): Double {
        return String.format(Locale.ENGLISH, "%.2f", value).toDouble()
    }
}