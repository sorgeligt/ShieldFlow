package com.sorgeligt.shieldflow.network.adapters

public class ListTelemetryNetworkEventsAdapter(
    private val paths: List<String>
) : TelemetryNetworkEventsAdapter {

    override fun getPaths(): List<String> = paths
}
