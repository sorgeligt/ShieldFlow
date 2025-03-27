package com.sorgeligt.shieldflow.assertion.handler

public object ThrowingAssertionEventInterrupter: AssertionEventInterrupter {
    override fun interrupt(event: AssertionEvent) {
        throw event.throwable
    }
}
