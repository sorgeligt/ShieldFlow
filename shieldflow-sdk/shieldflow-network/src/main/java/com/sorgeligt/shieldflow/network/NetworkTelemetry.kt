package com.sorgeligt.shieldflow.network

import android.content.Context
import android.net.ConnectivityManager
import com.sorgeligt.shieldflow.core.analyst.Analyst
import com.sorgeligt.shieldflow.core.analyst.AnalystImplScope
import com.sorgeligt.shieldflow.core.time.TimeProvider
import com.sorgeligt.shieldflow.core.time.TimeProviderImplScope
import com.sorgeligt.shieldflow.network.adapters.ListTelemetryNetworkEventsAdapter
import com.sorgeligt.shieldflow.network.adapters.TelemetryNetworkEventsAdapter
import com.sorgeligt.shieldflow.network.measure_events.MeasureEventListener
import com.sorgeligt.shieldflow.network.measure_events.NotifyOnResponseInterceptor
import com.sorgeligt.shieldflow.network.network_info.AndroidNetworkInfoRepository
import okhttp3.Call
import okhttp3.OkHttpClient

public object NetworkTelemetry : AnalystImplScope, TimeProviderImplScope {

    private lateinit var config: Config

    public fun init(config: Config) {
        this.config = config
    }

    private fun requireInit() {
        check(::config.isInitialized) { "You have to call init(config: Config) method first!" }
    }

    public fun addPath(vararg path: String) {
        requireInit()
        config.measureEventListener.addPath(*path)
    }

    public fun measure(client: OkHttpClient.Builder): OkHttpClient.Builder {
        requireInit()
        client.run {
            eventListener(config.measureEventListener)
            addInterceptor(NotifyOnResponseInterceptor(config.measureEventListener))
        }
        return client
    }

    public fun <T> measure(call: Call, stage: String, action: () -> T): T {
        requireInit()
        val startTimeMs = config.timeProvider.nowMillis()
        val res = action()
        val durationMs = config.timeProvider.nowMillis() - startTimeMs

        config.measureEventListener
            .onStageFinished(
                call = call,
                stage = stage,
                durationMs = durationMs
            )

        return res
    }

    public class Config private constructor(
        internal val timeProvider: TimeProvider,
        internal val measureEventListener: MeasureEventListener,
    ) {

        public class Builder(
            private val context: Context,
        ) {

            private var timeProvider: TimeProvider = systemTimeProvider
            private var analysts: List<Analyst> = listOf(analyst)
            private var shieldflowNetworkEventsAdapter: TelemetryNetworkEventsAdapter = ListTelemetryNetworkEventsAdapter(emptyList())

            public fun timeProvider(timeProvider: TimeProvider): Builder {
                this.timeProvider = timeProvider
                return this
            }

            public fun additionalAnalysts(analysts: List<Analyst>): Builder {
                this.analysts = analysts + analyst
                return this
            }

            public fun shieldflowNetworkEventsAdapter(
                shieldflowNetworkEventsAdapter: TelemetryNetworkEventsAdapter
            ): Builder {
                this.shieldflowNetworkEventsAdapter = shieldflowNetworkEventsAdapter
                return this
            }

            public fun build(): Config = Config(
                timeProvider = timeProvider,
                measureEventListener = MeasureEventListener(
                    analysts = analysts,
                    networkInfoRepository = AndroidNetworkInfoRepository(
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    ),
                    timeProvider = systemTimeProvider,
                    shieldflowNetworkEventsAdapter = shieldflowNetworkEventsAdapter,
                ),
            )
        }
    }
}
