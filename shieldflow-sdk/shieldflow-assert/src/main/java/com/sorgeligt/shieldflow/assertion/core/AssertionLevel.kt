package com.sorgeligt.shieldflow.assertion.core

@JvmInline
public value class AssertionLevel(public val value: Int) {
    public companion object
}

public val AssertionLevel.Companion.assert: AssertionLevel
    get() = AssertionLevel(0)
public val AssertionLevel.Companion.check: AssertionLevel
    get() = AssertionLevel(1)
public val AssertionLevel.Companion.require: AssertionLevel
    get() = AssertionLevel(2)
public val AssertionLevel.Companion.error: AssertionLevel
    get() = AssertionLevel(3)
public val AssertionLevel.Companion.todo: AssertionLevel
    get() = AssertionLevel(4)
