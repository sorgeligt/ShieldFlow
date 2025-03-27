@file:Suppress("NOTHING_TO_INLINE")

package com.sorgeligt.shieldflow.assertion

import com.sorgeligt.shieldflow.assertion.core.AssertionLevel
import com.sorgeligt.shieldflow.assertion.core.assert

public inline fun Assertions.assert(value: Boolean) {
    assert(value) { "Assertion failed" }
}

public inline fun Assertions.assert(value: Boolean, lazyMessage: () -> Any) {
    if (!value) {
        val message = lazyMessage()
        val exception = AssertionError(message)
        assertionHandler.handleAssert(AssertionLevel.assert, exception)
    }
}
