package com.sorgeligt.shieldflow.assertion.handler

import com.sorgeligt.shieldflow.assertion.core.*

public object NoOpAssertionHandler: AssertionHandler {
    public override fun handleAssert(level: AssertionLevel, error: Throwable) {
        // do nothing
    }
}
