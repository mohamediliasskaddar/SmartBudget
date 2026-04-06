// util/DateUtils.kt
package com.example.smartbudget.util

import java.util.Calendar

object DateUtils {

    /**
     * Retourne les bornes epoch (startMs inclusif, endMs exclusif)
     * pour un mois donné.
     */
    fun monthBounds(year: Int, month: Int): Pair<Long, Long> {
        val start = Calendar.getInstance().apply {
            set(year, month - 1, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val end = Calendar.getInstance().apply {
            set(year, month - 1, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.MONTH, 1)
        }.timeInMillis

        return Pair(start, end)
    }

    fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
    fun currentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

    /** Format "YYYY-MM" pour MonthlyBudget */
    fun toMonthKey(year: Int, month: Int): String =
        "%04d-%02d".format(year, month)
}