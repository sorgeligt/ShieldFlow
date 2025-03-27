package com.sorgeligt.shieldflow.network

import com.sorgeligt.shieldflow.network.url_tree.Node

internal fun buildTreeString(node: Node, prefix: String = "", isTail: Boolean = true): String {
    val valueWithPrefix = if (node is Node.VariableNode) "{${node.value}}" else node.value
    var result = ""
    result += ("${prefix}${if (isTail) "└─ " else "├─ "}${valueWithPrefix}\n")
    val children = node.children.toList()
    for (i in 0 until children.size - 1) {
        result += buildTreeString(
            node = children[i],
            prefix = "${prefix}${if (isTail) "  " else "│ "}",
            isTail = false
        )
    }
    if (children.isNotEmpty()) {
        result += buildTreeString(
            node = children.last(),
            prefix = "${prefix}${if (isTail) "  " else "│ "}",
            isTail = true
        )
    }
    return result
}
