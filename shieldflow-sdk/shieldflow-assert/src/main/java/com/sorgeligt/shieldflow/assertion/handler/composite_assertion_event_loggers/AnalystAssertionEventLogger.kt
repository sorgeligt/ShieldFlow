package com.sorgeligt.shieldflow.assertion.handler.composite_assertion_event_loggers

import com.sorgeligt.shieldflow.assertion.AssertTelemetry
import com.sorgeligt.shieldflow.assertion.assertAnalyst
import com.sorgeligt.shieldflow.assertion.handler.AssertionEvent
import com.sorgeligt.shieldflow.assertion.handler.AssertionEventLogger
import com.sorgeligt.shieldflow.core.analyst.AnalystImplScope

public object AnalystAssertionEventLogger: AssertionEventLogger {
    public override fun log(event: AssertionEvent) {
        assertAnalyst?.log("",mapOf(event.level.toString() to event.throwable))
    }
}
