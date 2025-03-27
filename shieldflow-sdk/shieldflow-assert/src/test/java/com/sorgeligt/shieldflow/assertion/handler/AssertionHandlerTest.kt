package com.sorgeligt.shieldflow.assertion.handler

import com.sorgeligt.shieldflow.assertion.*
import com.sorgeligt.shieldflow.assertion.core.*
import com.sorgeligt.shieldflow.assertion.handler.composite_assertion_event_loggers.NoOpAssertionEventLogger
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class AssertionHandlerTest {
    @Test
    fun testEventLoggerHandling() {
        val expectedMessage = "Message"

        Assertions.assertionHandler = DefaultAssertionHandler(
            logger = { event ->
                assertEquals(event.level, AssertionLevel.assert)

                assertIs<AssertionError>(event.throwable)
                assertEquals(event.throwable.message, expectedMessage)
            },
            interrupter = NoOpAssertionEventInterrupter,
        )

        Assertions.assert(false) { expectedMessage }
    }

    @Test
    fun testEventInterrupterHandling() {
        val expectedMessage = "Message"

        Assertions.assertionHandler = DefaultAssertionHandler(
            logger = NoOpAssertionEventLogger,
            interrupter = { event ->
                assertEquals(event.level, AssertionLevel.assert)

                assertIs<AssertionError>(event.throwable)
                assertEquals(event.throwable.message, expectedMessage)
            },
        )

        Assertions.assert(false) { expectedMessage }
    }

    @Test
    fun testEventInterrupter() {
        Assertions.assertionHandler = DefaultAssertionHandler(
            logger = NoOpAssertionEventLogger,
            interrupter = ThrowingAssertionEventInterrupter,
        )

        assertFailsWith<AssertionError> {
            Assertions.assert(false)
        }
    }
}
