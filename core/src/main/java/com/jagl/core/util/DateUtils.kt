package com.jagl.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utilidades para manejar fechas
 */
object DateUtils {

    private const val DATE_FORMAT = "yyyy-MM-dd"

    /**
     * Obtiene la fecha actual en formato YYYY-MM-DD
     */
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(Date())
    }

    /**
     * Formatea una fecha en formato YYYY-MM-DD
     */
    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(date)
    }

    /**
     * Parsea una fecha en formato YYYY-MM-DD
     */
    fun parseDate(dateString: String): Date? {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
}