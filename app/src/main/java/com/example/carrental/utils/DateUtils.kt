package com.example.carrental.utils

import com.example.carrental.time.Clock
import java.util.Calendar
import java.util.Locale

object DateUtils {

    fun hasYearPassed(year: Int): Boolean {
        val normalized = normalizeYear(year)
        val now = Clock.getCalendarInstance()
        return normalized < now.get(Calendar.YEAR)
    }

    fun hasMonthPassed(year: Int, month: Int): Boolean {
        if (hasYearPassed(year)) {
            return true
        }

        val now = Clock.getCalendarInstance()
        return normalizeYear(year) == now.get(Calendar.YEAR) &&
                month < (now.get(Calendar.MONTH) + 1)
    }

    private fun normalizeYear(year: Int): Int {
        if (year in 0..99) {
            val now = Clock.getCalendarInstance()
            val currentYear = now.get(Calendar.YEAR).toString()
            val prefix = currentYear.substring(0, currentYear.length - 2)
            return String.format(Locale.US, "%s%02d", prefix, year).toInt()
        }
        return year
    }
}