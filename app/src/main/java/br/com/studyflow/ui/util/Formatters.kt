package br.com.studyflow.ui.util

import java.text.SimpleDateFormat
import java.util.*

fun formatDate(millis: Long): String = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(millis))

fun formatWeight(value: Double): String = if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()

fun utcDateToLocalMidnight(utcMillis: Long): Long {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utcMillis }
    return Calendar.getInstance().apply {
        set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH), 9, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
