@file:Suppress("NOTHING_TO_INLINE")

package com.sorgeligt.shieldflow.assertion

import com.sorgeligt.shieldflow.assertion.core.AssertionLevel
import com.sorgeligt.shieldflow.assertion.core.check
import com.sorgeligt.shieldflow.assertion.core.error
import com.sorgeligt.shieldflow.assertion.core.require

public inline fun Assertions.check(value: Boolean) {
    check(value) { "Check failed." }
}

public inline fun Assertions.check(value: Boolean, lazyMessage: () -> Any) {
    if (!value) {
        val message = lazyMessage()
        val exception = IllegalStateException(message.toString())
        assertionHandler.handleAssert(AssertionLevel.check, exception)
    }
}

public inline fun <T : Any> Assertions.checkNotNull(value: T?, fallback: () -> T): T {
    return checkNotNull(value, fallback) { "Required value was null." }
}

public inline fun <T : Any> Assertions.checkNotNull(value: T?, fallback: () -> T, lazyMessage: () -> Any): T {
    return if (value != null) {
        value
    } else {
        val message = lazyMessage()
        val exception = IllegalStateException(message.toString())
        assertionHandler.handleAssert(AssertionLevel.check, exception)

        fallback()
    }
}

public inline fun Assertions.require(value: Boolean) {
    require(value) { "Failed requirement." }
}

public inline fun Assertions.require(value: Boolean, lazyMessage: () -> Any) {
    if (!value) {
        val message = lazyMessage()
        val exception = IllegalArgumentException(message.toString())
        assertionHandler.handleAssert(AssertionLevel.require, exception)
    }
}

public inline fun <T : Any> Assertions.requireNotNull(value: T?, fallback: () -> T): T {
    return requireNotNull(value, fallback) { "Required value was null." }
}

public inline fun <T : Any> Assertions.requireNotNull(value: T?, fallback: () -> T, lazyMessage: () -> Any): T {
    return if (value != null) {
        value
    } else {
        val message = lazyMessage()
        val exception = IllegalArgumentException(message.toString())
        assertionHandler.handleAssert(AssertionLevel.require, exception)

        fallback()
    }
}

public inline fun Assertions.error(message: Any) {
    val exception = IllegalStateException(message.toString())
    assertionHandler.handleAssert(AssertionLevel.error, exception)
}
