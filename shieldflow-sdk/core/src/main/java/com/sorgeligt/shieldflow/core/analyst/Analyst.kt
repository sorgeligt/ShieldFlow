package com.sorgeligt.shieldflow.core.analyst

public interface Analyst {
    public fun log(event: String, params: Map<String, Any?>? = null)
}
