package com.jagl.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Utilities for handling dates
 */
object DateUtils {

    const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val DEFAULT_TIME_ZONE = "UTC"

    /**
     * Gets the current date in the specified format (defaults to YYYY-MM-DD)
     */
    fun getDateWithFormat(
        locale: Locale,
        date: Date,
        pattern: String = DATE_FORMAT_YYYY_MM_DD
    ): String {
        checkValidPattern(pattern)
        val dateFormat = getSimpleDateFormat(pattern, locale)
        return dateFormat.format(date)
    }

    /**
     * Formats a date in the specified format (defaults to YYYY-MM-DD)
     */
    fun formatDate(
        date: Date,
        locale: Locale,
        pattern: String = DATE_FORMAT_YYYY_MM_DD
    ): String {
        checkValidPattern(pattern)
        val dateFormat = getSimpleDateFormat(pattern, locale)
        return dateFormat.format(date)
    }

    /**
     * Parses a date in the specified format (defaults to YYYY-MM-DD)
     * @param dateString Date to parse.
     * @param pattern Pattern.
     * @return A {@code Date} parsed from the string.
     * @throws Exception if the format is invalid
     *
     */
    fun parseToDate(
        dateString: String,
        locale: Locale,
        pattern: String = DATE_FORMAT_YYYY_MM_DD,
    ): Date {
        checkValidPattern(pattern)
        val dateFormat = getSimpleDateFormat(pattern, locale)
        return dateFormat.parse(dateString)
            ?: throw IllegalArgumentException("Date string could not be parsed, please check and try again")
    }

    private fun checkValidPattern(pattern: String) {
        require(pattern.isNotEmpty()) { "Pattern must not be empty, please check and try again" }
    }

    private fun getSimpleDateFormat(pattern: String, locale: Locale): SimpleDateFormat {
        try {
            return SimpleDateFormat(pattern, locale).apply {
                timeZone = TimeZone.getTimeZone(DEFAULT_TIME_ZONE)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException(
                "Passed pattern is not valid, please check and try again", e
            )
        }
    }
}
