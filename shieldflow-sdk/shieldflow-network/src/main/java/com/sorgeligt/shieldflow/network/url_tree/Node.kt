package com.sorgeligt.shieldflow.network.url_tree

internal sealed interface Node {
    val value: String
    var children: List<Node>

    class VariableNode(
        override var value: String,
        override var children: List<Node> = emptyList(),
    ) : Node

    class ConstNode(
        override var value: String,
        override var children: List<Node> = emptyList(),
    ) : Node

    class LeafNode : Node {
        override var value: String = ""
        override var children: List<Node> = emptyList()
    }
}

internal fun String.splitPath(): List<Node> = split("/")
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .map { value ->
        if (value.contains("{") && value.contains("}")) {
            val fieldName = value.substring(value.indexOf("{") + 1, value.indexOf("}"))
            Node.VariableNode(fieldName, emptyList())
        } else {
            Node.ConstNode(value, emptyList())
        }
    }
