package com.salwa.adikapp.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    private val displayFormat = SimpleDateFormat("d MMM yyyy", Locale("id", "ID"))
    private val displayFormatWithTime = SimpleDateFormat("d MMM yyyy, HH:mm", Locale("id", "ID"))

    fun formatDate(millis: Long): String = displayFormat.format(millis)
    fun formatDateTime(millis: Long): String = displayFormatWithTime.format(millis)

    fun startOfDay(millis: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun endOfDay(millis: Long): Long = startOfDay(millis) + (24 * 60 * 60 * 1000L - 1)

    val dayNames = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
}
