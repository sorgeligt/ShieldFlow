package com.sorgeligt.shieldflow.assertion

import com.sorgeligt.shieldflow.assertion.core.*
import org.junit.BeforeClass
import org.junit.Test
import kotlin.reflect.KFunction
import kotlin.test.*

class AssertionTest {
    @Test
    fun testThatAssertStackTraceStartsWithCurrentMethodName() {
        Assertions.assert(false) { message(AssertionLevel.assert, this::testThatAssertStackTraceStartsWithCurrentMethodName) }
    }

    @Test
    fun testThatCheckStackTraceStartsWithCurrentMethodName() {
        Assertions.check(false) { message(AssertionLevel.check, this::testThatCheckStackTraceStartsWithCurrentMethodName) }
    }

    @Test
    fun testThatCheckNotNullStackTraceStartsWithCurrentMethodName() {
        val expected = "123"
        val actual = Assertions.checkNotNull(null, { expected }) { message(AssertionLevel.check, this::testThatCheckNotNullStackTraceStartsWithCurrentMethodName) }
        assertEquals(expected, actual)
    }

    @Test
    fun testThatRequireStackTraceStartsWithCurrentMethodName() {
        Assertions.require(false) { message(AssertionLevel.require, this::testThatRequireStackTraceStartsWithCurrentMethodName) }
    }

    @Test
    fun testThatRequireNotNullStackTraceStartsWithCurrentMethodName() {
        val expected = "123"
        val actual = Assertions.requireNotNull(null, { expected }) { message(AssertionLevel.require, this::testThatRequireNotNullStackTraceStartsWithCurrentMethodName) }
        assertEquals(expected, actual)
    }

    @Test
    fun testThatErrorStackTraceStartsWithCurrentMethodName() {
        Assertions.error(message(AssertionLevel.error, this::testThatErrorStackTraceStartsWithCurrentMethodName))
    }

    @Test
    fun testThatTodoStackTraceStartsWithCurrentMethodName() {
        Assertions.todo(message(AssertionLevel.todo, this::testThatTodoStackTraceStartsWithCurrentMethodName))
    }

    private fun message(level: AssertionLevel, method: KFunction<Unit>): String {
        return "${this::class.qualifiedName!!}.${method.name}$${level.value}"
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun setup() {
            Assertions.assertionHandler = mockAssertionHandler { level, error ->
                val firstTrace = error.stackTrace[0]
                val messageParts = error.message!!.split("$")

                val typeName = messageParts[0].split(".")
                val className = typeName.dropLast(1).joinToString(".")
                val methodName = typeName.last()

                assertContains(className, firstTrace.className)
                assertEquals(methodName, firstTrace.methodName)

                val levelValue = messageParts[1].toInt()
                assertEquals(level.value, levelValue)
            }
        }

        private fun mockAssertionHandler(block: (AssertionLevel, Throwable) -> Unit): AssertionHandler = object :
            AssertionHandler {
            override fun handleAssert(level: AssertionLevel, error: Throwable) = block(level, error)
        }
    }
}
