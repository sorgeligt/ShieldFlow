package com.sorgeligt.shieldflow.assertion

import com.sorgeligt.shieldflow.assertion.core.*
import com.sorgeligt.shieldflow.assertion.handler.*

public object Assertions {
    public val ENABLED: Boolean = javaClass.desiredAssertionStatus()

    public var assertionHandler: AssertionHandler = NoOpAssertionHandler
}
