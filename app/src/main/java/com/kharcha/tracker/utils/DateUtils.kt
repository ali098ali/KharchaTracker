package com.kharcha.tracker.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val display = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val monthYear = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    fun formatDisplay(date: Date) = display.format(date)
    fun formatMonthYear(year: Int, month: Int): String {
        val c = Calendar.getInstance().apply { set(year, month, 1) }
        return monthYear.format(c.time)
    }

    fun monthRange(year: Int, month: Int): Pair<Long, Long> {
        val start = Calendar.getInstance().apply {
            set(year, month, 1, 0, 0, 0); set(Calendar.MILLISECOND, 0)
        }
        val end = Calendar.getInstance().apply {
            timeInMillis = start.timeInMillis; add(Calendar.MONTH, 1)
        }
        return start.timeInMillis to end.timeInMillis
    }

    fun currentYear() = Calendar.getInstance().get(Calendar.YEAR)
    fun currentMonth() = Calendar.getInstance().get(Calendar.MONTH)
}
