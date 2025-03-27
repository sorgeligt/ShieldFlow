package com.sorgeligt.shieldflow.assertion.core

public interface AssertionHandler {
    public fun handleAssert(
        level: AssertionLevel,
        error: Throwable,
    )
}
