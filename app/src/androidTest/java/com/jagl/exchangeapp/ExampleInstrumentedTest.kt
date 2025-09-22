package com.jagl.exchangeapp

import androidx.test.platform.app.InstrumentationRegistry
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test


class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertThat("com.jagl.core.test").isEqualTo(appContext.packageName)
    }
}