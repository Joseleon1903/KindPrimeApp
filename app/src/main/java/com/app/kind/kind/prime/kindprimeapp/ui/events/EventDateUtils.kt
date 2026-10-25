package com.app.kind.kind.prime.kindprimeapp.ui.events

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private val eventDateFormatter =
    DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es"))

private fun startOfDayMillis(date: LocalDate): Long =
    date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

/** Fecha por defecto para un evento nuevo: mañana. */
fun defaultEventDateMillis(): Long = startOfDayMillis(LocalDate.now().plusDays(1))

/** El DatePicker de Material 3 trabaja con medianoche UTC; lo convertimos al día local. */
fun pickerMillisToLocalDateMillis(pickerMillis: Long): Long =
    Instant.ofEpochMilli(pickerMillis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
        .let(::startOfDayMillis)

/** Convierte una fecha local guardada al formato que espera el DatePicker (UTC). */
fun localDateMillisToPickerMillis(localMillis: Long): Long =
    Instant.ofEpochMilli(localMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()

fun formatEventDate(millis: Long): String =
    Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(eventDateFormatter)

/** Días de diferencia entre hoy y la fecha del evento (negativo si ya pasó). */
fun daysUntil(millis: Long): Long =
    ChronoUnit.DAYS.between(
        LocalDate.now(),
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate(),
    )
