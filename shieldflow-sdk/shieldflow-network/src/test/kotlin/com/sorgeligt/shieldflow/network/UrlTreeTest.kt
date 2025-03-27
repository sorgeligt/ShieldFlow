package com.sorgeligt.shieldflow.network

import com.sorgeligt.shieldflow.network.url_tree.Node
import com.sorgeligt.shieldflow.network.url_tree.addPath
import com.sorgeligt.shieldflow.network.url_tree.findMatchingPattern
import kotlin.test.Test
import kotlin.test.assertEquals

class UrlTreeTest {

    @Test
    fun checkTree() {
        val root = Node.ConstNode("")
        root.addPath("some/path/count/diff")
        root.addPath("some/path/count/{color}")

        root.addPath("some/{name}/count")
        root.addPath("some/{name}/sum/{category}/save")
        root.addPath("some/{name}/sum/category/edit")
        root.addPath("some/{name}/sum/category/save/all")

        root.addPath("some/{size}/{id}/{name}")
        root.addPath("some/{size}/{id}/option/full")
        assertEquals(
            expected = buildTreeString(root),
            actual =
            """└─${' '}
  └─ some
    ├─ path
    │ └─ count
    │   ├─ diff
    │   │ └─${' '}
    │   └─ {color}
    │     └─${' '}
    ├─ {name}
    │ ├─ count
    │ │ └─${' '}
    │ └─ sum
    │   ├─ {category}
    │   │ └─ save
    │   │   └─${' '}
    │   └─ category
    │     ├─ edit
    │     │ └─${' '}
    │     └─ save
    │       └─ all
    │         └─${' '}
    └─ {size}
      └─ {id}
        ├─ {name}
        │ └─${' '}
        └─ option
          └─ full
            └─${' '}
"""
        )
    }

    @Test
    fun checkUrlsMatching() {
        val root = Node.ConstNode("")
        root.addPath("some/path/count/diff")
        root.addPath("some/path/count/{color}")

        root.addPath("some/{name}/count")
        root.addPath("some/{name}/sum/{category}/save")
        root.addPath("some/{name}/sum/category/edit")
        root.addPath("some/{name}/sum/category/save/all")
        root.addPath("{version}/{name}/sum/category/save/all")
        root.addPath("{version}//{name}/sum/category/save/all")

        root.addPath("some/{size}/{id}/{name}")
        root.addPath("some/{size}/{id}/option/full")

        assertEquals(root.findMatchingPattern("/some/path/count/diff"), "/some/path/count/diff")
        assertEquals(root.findMatchingPattern("some/path/count/red"), "/some/path/count/{color}")
        assertEquals(root.findMatchingPattern("some/path/wrong_name/red"), "/some/{size}/{id}/{name}")
        assertEquals(root.findMatchingPattern("/some/random_name1/count"), "/some/{name}/count")
        assertEquals(root.findMatchingPattern("some/random_name2//sum/category/save"), "/some/{name}/sum/{category}/save")
        assertEquals(root.findMatchingPattern("some/random_name3/sum/category/edit"), "/some/{name}/sum/category/edit")
        assertEquals(root.findMatchingPattern("/some/random_name4//sum/category/save/all"), "/some/{name}/sum/category/save/all")
        assertEquals(root.findMatchingPattern("some/random_name5/sum/wrong_name/save/all"), null)

        assertEquals(root.findMatchingPattern("some/1/2/3"), "/some/{size}/{id}/{name}")
        assertEquals(root.findMatchingPattern("some/1//2//option/full"), "/some/{size}/{id}/option/full")
        assertEquals(root.findMatchingPattern("some/1/2/option/wrong_name/full"), null)
        assertEquals(root.findMatchingPattern("2.0"), null)
        root.addPath("{version}")
        assertEquals(root.findMatchingPattern("2.0"), "/{version}")
    }
}
