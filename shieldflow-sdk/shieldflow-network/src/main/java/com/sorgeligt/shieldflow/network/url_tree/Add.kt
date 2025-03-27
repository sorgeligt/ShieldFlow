package com.sorgeligt.shieldflow.network.url_tree

internal fun Node.addPath(path: String) {
    val segments = path.splitPath()
    var currentNode = this

    for (segment in segments) {
        val existingNode = currentNode.children.find { child ->
            child.value == segment.value && child::class == segment::class
        }

        currentNode = if (existingNode != null) {
            existingNode
        } else {
            currentNode.children += segment
            segment
        }
    }
    currentNode.children += Node.LeafNode()
}
