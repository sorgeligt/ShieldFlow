package com.tmdb.movie

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import com.sorgeligt.shieldflow.Telemetry
import com.sorgeligt.shieldflow.assertion.AssertTelemetry
import com.sorgeligt.shieldflow.assertion.Assertions
import com.sorgeligt.shieldflow.assertion.assert
import com.sorgeligt.shieldflow.assertion.handler.AssertionEvent
import com.sorgeligt.shieldflow.assertion.handler.AssertionEventInterrupter
import com.sorgeligt.shieldflow.assertion.handler.AssertionEventLogger
import com.sorgeligt.shieldflow.assertion.handler.DefaultAssertionHandler
import com.sorgeligt.shieldflow.assertion.handler.NoOpAssertionEventInterrupter
import com.sorgeligt.shieldflow.assertion.handler.ThrowingAssertionEventInterrupter
import com.sorgeligt.shieldflow.assertion.handler.composite_assertion_event_loggers.AnalystAssertionEventLogger
import com.sorgeligt.shieldflow.core.analyst.Analyst
import com.sorgeligt.shieldflow.core.time.TimeProvider
import com.sorgeligt.shieldflow.network.NetworkTelemetry
import com.sorgeligt.shieldflow.network.adapters.RetrofitContract
import com.sorgeligt.shieldflow.network.adapters.RetrofitTelemetryNetworkEventsAdapter
import com.tmdb.movie.network.ApiService
import dagger.hilt.android.HiltAndroidApp
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.time.Clock

@HiltAndroidApp
class TMDBMovieApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        Telemetry.init(this)
        val shieldflowNetworkEventsAdapter = RetrofitTelemetryNetworkEventsAdapter(
            retrofitContracts = listOf(
                RetrofitContract(ApiService::class.java, ""),
            ),
            supportedAnnotations = listOf(
                GET::class.java,
                DELETE::class.java,
                PUT::class.java,
                POST::class.java
            ),
        )
        NetworkTelemetry.init(
            NetworkTelemetry.Config.Builder(this)
                .timeProvider(object : TimeProvider {
                    override fun nowMillis(): Long = System.currentTimeMillis()
                })
                .additionalAnalysts(
                    listOf(
                        object : Analyst {
                            override fun log(
                                event: String,
                                params: Map<String, Any?>?
                            ) {
                                Log.d("ShieldFlow", "Analyst: $event $params")
                            }

                        }
                    )
                )
                .shieldflowNetworkEventsAdapter(
                    shieldflowNetworkEventsAdapter
                )
                .build()
        )
        Assertions.assertionHandler = DefaultAssertionHandler(
            logger = object : AssertionEventLogger {
                override fun log(event: AssertionEvent) {
                    Log.d("ShieldFlow", "AssertionEvent: $event")
                }
            },
            interrupter = NoOpAssertionEventInterrupter
        )
        Assertions.assert(false, {"Emulator DETECTED!"} )
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .logger(DebugLogger())
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

}
