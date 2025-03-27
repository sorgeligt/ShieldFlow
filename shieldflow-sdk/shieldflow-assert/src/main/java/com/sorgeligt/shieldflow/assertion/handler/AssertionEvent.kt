package com.sorgeligt.shieldflow.assertion.handler

import com.sorgeligt.shieldflow.assertion.core.*

public data class AssertionEvent(
    public val level: AssertionLevel,
    public val throwable: Throwable,
)
