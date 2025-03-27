@file:Suppress("NOTHING_TO_INLINE")

package com.sorgeligt.shieldflow.assertion

import com.sorgeligt.shieldflow.assertion.core.AssertionLevel
import com.sorgeligt.shieldflow.assertion.core.todo

public inline fun Assertions.todo(reason: Any) {
    val exception = NotImplementedError("An operation is not implemented: $reason")
    assertionHandler.handleAssert(AssertionLevel.todo, exception)
}
