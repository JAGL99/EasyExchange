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

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DateUtilsUnitTest {

    private var dateUtilInstance: DateUtils? = null

    @BeforeEach
    fun setUp() {
        dateUtilInstance = DateUtils
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
        assertThrows<IllegalArgumentException> { dateUtilInstance!!.getCurrentDate("") }
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
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "HH:mm:ss",
        "hh:mm a",
        "yyyy-MM-dd'T'HH:mm:ssXXX"
    )
    fun `request current date, get currentDate`(pattern: String){
        assertDoesNotThrow {
            with(dateUtilInstance!!){
                val date = Date()
                val currentDate = getCurrentDate(pattern)
                val formatedDate = formatDate(date, pattern)
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
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "HH:mm:ss",
        "hh:mm a",
        "yyyy-MM-dd'T'HH:mm:ssXXX"
    )
    fun `have string date, get parsed date`(pattern: String){
        assertDoesNotThrow {
            with(dateUtilInstance!!){
                val currentDate = getCurrentDate(pattern)
                val parseDate = parseToDate(currentDate, pattern)
                assertThat(parseDate).isInstanceOf(Date::class)
                val formatedDate = formatDate(parseDate, pattern)
                assertThat(formatedDate).isEqualTo(currentDate)
            }
        }
    }

}