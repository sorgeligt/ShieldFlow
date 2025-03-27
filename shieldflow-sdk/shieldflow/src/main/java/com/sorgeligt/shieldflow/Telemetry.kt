package com.sorgeligt.shieldflow

import android.app.Application
import com.sorgeligt.shieldflow.core.ActivityLifecycleLogger
import com.sorgeligt.shieldflow.core.analyst.AnalystImplScope

public object Telemetry : AnalystImplScope {

    public fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleLogger(analyst))
        logStartup()
    }

    private fun logStartup() {
        analyst.log("core.app_launch")
    }
}
