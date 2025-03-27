package com.sorgeligt.shieldflow.assertion.handler

import com.sorgeligt.shieldflow.assertion.core.*

public class DefaultAssertionHandler(
    public val logger: AssertionEventLogger,
    public val interrupter: AssertionEventInterrupter,
): AssertionHandler {
    public override fun handleAssert(level: AssertionLevel, error: Throwable) {
        val event = AssertionEvent(level, error)
        logger.log(event)
        interrupter.interrupt(event)
    }
}
