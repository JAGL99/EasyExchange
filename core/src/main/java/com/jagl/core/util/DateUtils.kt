package com.jagl.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utilidades para manejar fechas
 */
object DateUtils {

    const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"

    /**
     * Obtiene la fecha actual en el formato especificado (por defecto YYYY-MM-DD)
     */
    fun getCurrentDate(pattern: String = DATE_FORMAT_YYYY_MM_DD): String {
        checkValidPattern(pattern)
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(Date())
    }

    /**
     * Formatea una fecha en formato especificado (por defecto YYYY-MM-DD)
     */
    fun formatDate(date: Date, pattern: String = DATE_FORMAT_YYYY_MM_DD): String {
        checkValidPattern(pattern)
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
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
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.parse(dateString)

    }

    private fun checkValidPattern(pattern: String) {
        if (pattern.isEmpty()) throw IllegalArgumentException("Passed pattern is empty, please check and try again")
        SimpleDateFormat(pattern)
    }
}