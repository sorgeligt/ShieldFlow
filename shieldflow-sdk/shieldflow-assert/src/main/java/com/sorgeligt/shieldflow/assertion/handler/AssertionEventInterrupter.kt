package com.sorgeligt.shieldflow.assertion.handler

public fun interface AssertionEventInterrupter {
    public fun interrupt(event: AssertionEvent)
}

public object NoOpAssertionEventInterrupter: AssertionEventInterrupter {
    public override fun interrupt(event: AssertionEvent) {
        // do nothing
    }
}
