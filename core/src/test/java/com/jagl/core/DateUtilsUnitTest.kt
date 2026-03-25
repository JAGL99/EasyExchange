package com.jagl.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.jagl.core.util.DateUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.Date
import java.util.Locale

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DateUtilsUnitTest {

    private var dateUtilInstance: DateUtils? = null
    private var locale: Locale? = null

    @BeforeEach
    fun setUp() {
        dateUtilInstance = DateUtils
        locale = Locale("es","MXN")
    }

    @AfterEach
    fun tearDown() {
        dateUtilInstance = null
    }

    @Test
    fun `call dateUtilInstance, dont throw NullPointerException`() {
        assertDoesNotThrow { dateUtilInstance!!::class.java }
    }

    @Test
    fun `request current date with empty pattern, throw IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> { dateUtilInstance!!.getDateWithFormat(locale = locale!!,pattern = "", date = Date()) }
    }

    @ParameterizedTest
    @CsvSource(
        "yyyy-MM-dd",
        "dd/MM/yyyy",
        "MM-dd-yyyy",
        "yyyyMMdd",
        "EEE, MMM d, yyyy",
        "EEEE, MMMM d, yyyy",
        "dd-MM-yyyy HH:mm",
        "yyyy-MM-dd'T'HH:mm:ss",
        "HH:mm:ss",
        "hh:mm a",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    fun `request current date, get currentDate`(pattern: String) {
        assertDoesNotThrow {
            with(dateUtilInstance!!) {
                val date = Date()
                val currentDate = getDateWithFormat(locale = locale!!,date, pattern)
                val formatedDate = formatDate(date, locale!!,pattern)
                assertThat(currentDate).isEqualTo(formatedDate)
            }

        }


    }

    @ParameterizedTest
    @CsvSource(
        "yyyy-MM-dd",
        "dd/MM/yyyy",
        "MM-dd-yyyy",
        "yyyyMMdd",
        "EEE, MMM d, yyyy",
        "EEEE, MMMM d, yyyy",
        "dd-MM-yyyy HH:mm",
        "yyyy-MM-dd'T'HH:mm:ss",
        "HH:mm:ss",
        "hh:mm a",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    )
    fun `have string date, get parsed date`(pattern: String) {
        assertDoesNotThrow {
            with(dateUtilInstance!!) {
                val currentDate = getDateWithFormat(locale = locale!!,pattern = pattern, date = Date())
                val parseDate = parseToDate(currentDate, locale = locale!!,pattern)
                assertThat(parseDate).isInstanceOf(Date::class)
                val formatedDate = formatDate(parseDate, locale = locale!!,pattern)
                assertThat(formatedDate).isEqualTo(currentDate)
            }
        }
    }

}