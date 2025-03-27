package com.sorgeligt.shieldflow.network

import com.sorgeligt.shieldflow.network.url_tree.Node
import com.sorgeligt.shieldflow.network.url_tree.splitPath
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseBasePathTest {

    @Test
    fun parsePath() {
        val path = "some/url/{id}/sum/{category}"
        checkPath(path)
    }

    @Test
    fun parsePathWithSlash() {
        val path = "/some/url/{id}/sum/{category}/"
        checkPath(path)
    }

    private fun checkPath(path: String) {
        val nodes = path.splitPath()
        assert(nodes.size == 5)
        assertEquals(
            nodes.map { it::class.java },
            listOf(
                Node.ConstNode::class.java,
                Node.ConstNode::class.java,
                Node.VariableNode::class.java,
                Node.ConstNode::class.java,
                Node.VariableNode::class.java
            )
        )
        assertEquals(nodes.map { it.value }, listOf("some", "url", "id", "sum", "category"))
    }
}
