package br.com.studyflow.ui.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatarData(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")).format(Date(millis))

fun formatarPeso(valor: Double): String =
    if (valor % 1.0 == 0.0) valor.toInt().toString() else valor.toString()

// O DatePicker devolve a meia-noite em UTC; guardamos o mesmo dia às 9h no fuso do aparelho.
fun dataUtcParaHorarioLocal(utcMillis: Long): Long {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utcMillis }
    return Calendar.getInstance().apply {
        set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH), 9, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

// Dias de calendário entre hoje e o prazo; negativo quando a avaliação está atrasada.
fun diasAtePrazo(dataMillis: Long, agora: Long = System.currentTimeMillis()): Long {
    val zona = ZoneId.systemDefault()
    val hoje = Instant.ofEpochMilli(agora).atZone(zona).toLocalDate()
    val prazo = Instant.ofEpochMilli(dataMillis).atZone(zona).toLocalDate()
    return ChronoUnit.DAYS.between(hoje, prazo)
}

fun descreverPrazo(dias: Long): String = when {
    dias < -1 -> "Atrasada há ${-dias} dias"
    dias == -1L -> "Atrasada há 1 dia"
    dias == 0L -> "Vence hoje"
    dias == 1L -> "Vence amanhã"
    else -> "Faltam $dias dias"
}
