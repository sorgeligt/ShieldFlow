package com.sorgeligt.shieldflow.assertion

import android.app.Application
import com.sorgeligt.shieldflow.core.analyst.Analyst
import com.sorgeligt.shieldflow.core.analyst.AnalystImplScope

internal var assertAnalyst: Analyst? = null

public object AssertTelemetry : AnalystImplScope {
    init {
        assertAnalyst = analyst
    }
}
