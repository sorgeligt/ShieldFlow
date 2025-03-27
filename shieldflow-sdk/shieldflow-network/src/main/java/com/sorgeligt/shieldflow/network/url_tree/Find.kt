package com.sorgeligt.shieldflow.network.url_tree

internal fun Node.findMatchingPattern(url: String): String? {
    val parts = url.split("/").filter { it.isNotEmpty() }
    return findMatchingPatternRecursive(this, parts, 0)?.let { "/$it" }
}

private fun findMatchingPatternRecursive(node: Node, parts: List<String>, index: Int): String? {
    if (index == parts.size && node.children.any { it is Node.LeafNode }) {
        return ""
    }

    val currentPart = parts.getOrNull(index) ?: return null
    for (child in node.children) {
        when (child) {
            is Node.ConstNode -> {
                if (child.value == currentPart) {
                    findMatchingPatternRecursive(
                        node = child,
                        parts = parts,
                        index = index + 1
                    )?.let { remainingPattern ->
                        if (remainingPattern.isNotEmpty()) {
                            return "${child.value}/$remainingPattern"
                        } else {
                            return child.value
                        }
                    }
                }
            }

            is Node.VariableNode -> {
                findMatchingPatternRecursive(
                    node = child,
                    parts = parts,
                    index = index + 1
                )?.let { remainingPattern ->
                    if (remainingPattern.isNotEmpty()) {
                        return "{${child.value}}/$remainingPattern"
                    } else {
                        return "{${child.value}}"
                    }
                }
            }
            is Node.LeafNode -> {
                return null
            }
        }
    }
    return null
}
