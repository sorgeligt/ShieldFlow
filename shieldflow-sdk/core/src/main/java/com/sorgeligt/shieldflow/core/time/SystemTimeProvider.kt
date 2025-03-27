package com.sorgeligt.shieldflow.core.time

internal object SystemTimeProvider : TimeProvider {
    override fun nowMillis(): Long = System.currentTimeMillis()
}
