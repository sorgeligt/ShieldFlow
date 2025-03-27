package com.sorgeligt.shieldflow.core

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.sorgeligt.shieldflow.core.analyst.Analyst

internal class ActivityLifecycleLogger(
    private val analyst: Analyst,
) : ActivityLifecycleCallbacks {

    private var startedActivitiesCount: Int = 0

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        // do nothing
    }

    override fun onActivityStarted(p0: Activity) {
        ++startedActivitiesCount
        if (startedActivitiesCount == 1) {
            analyst.log("core.app_resume")
        }
    }

    override fun onActivityResumed(p0: Activity) {
        // do nothing
    }

    override fun onActivityPaused(p0: Activity) {
        // do nothing
    }

    override fun onActivityStopped(p0: Activity) {
        --startedActivitiesCount
        if (startedActivitiesCount == 0) {
            analyst.log("core.app_pause")
        }
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        // do nothing
    }

    override fun onActivityDestroyed(p0: Activity) {
        // do nothing
    }
}
