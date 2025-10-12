package com.jagl.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Utilidades para manejar fechas
 */
object DateUtils {

    const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val DEFAULT_TIME_ZONE = "UTC"

    /**
     * Obtiene la fecha actual en el formato especificado (por defecto YYYY-MM-DD)
     */
    fun getCurrentDate(pattern: String = DATE_FORMAT_YYYY_MM_DD): String {
        checkValidPattern(pattern)
        val dateFormat = getSimpleDateFormat(pattern)
        return dateFormat.format(Date())
    }

    /**
     * Formatea una fecha en formato especificado (por defecto YYYY-MM-DD)
     */
    fun formatDate(date: Date, pattern: String = DATE_FORMAT_YYYY_MM_DD): String {
        checkValidPattern(pattern)
        val dateFormat = getSimpleDateFormat(pattern)
        return dateFormat.format(date)
    }

    /**
     * Parsea una fecha en el formato especificado (por defecto YYYY-MM-DD)
     * @param dateString Fecha a parsear.
     * @param pattern Patrón.
     * @return A {@code Date} parsed from the string.
     * @throws Exception si el formato es inválido
     *
     */
    fun parseToDate(dateString: String, pattern: String = DATE_FORMAT_YYYY_MM_DD): Date {
        checkValidPattern(pattern)
        val dateFormat = getSimpleDateFormat(pattern)
        return dateFormat.parse(dateString)
            ?: throw IllegalArgumentException("Date string could not be parsed, please check and try again")
    }

    private fun checkValidPattern(pattern: String) {
        require(pattern.isNotEmpty()) { "Pattern must not be empty, please check and try again" }
    }

    private fun getSimpleDateFormat(
        pattern: String,
        locale: Locale = Locale.getDefault()
    ): SimpleDateFormat {
        try {
            return SimpleDateFormat(pattern, locale).apply {
                timeZone = TimeZone.getTimeZone(DEFAULT_TIME_ZONE)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException(
                "Passed pattern is not valid, please check and try again",
                e
            )
        }
    }
}