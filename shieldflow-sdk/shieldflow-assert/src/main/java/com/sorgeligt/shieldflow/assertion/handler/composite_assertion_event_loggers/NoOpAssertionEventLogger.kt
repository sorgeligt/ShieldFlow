package com.sorgeligt.shieldflow.assertion.handler.composite_assertion_event_loggers

import com.sorgeligt.shieldflow.assertion.handler.AssertionEvent
import com.sorgeligt.shieldflow.assertion.handler.AssertionEventLogger

public object NoOpAssertionEventLogger: AssertionEventLogger {
    public override fun log(event: AssertionEvent) {
        // do nothing
    }
}
