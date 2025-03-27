package com.sorgeligt.shieldflow.assertion.handler.composite_assertion_event_loggers

import com.sorgeligt.shieldflow.assertion.handler.AssertionEvent
import com.sorgeligt.shieldflow.assertion.handler.AssertionEventLogger

public class CompositeAssertionEventLogger(
    private val items: List<AssertionEventLogger>,
): AssertionEventLogger {
    public override fun log(event: AssertionEvent) {
        items.forEach { it.log(event) }
    }
}
