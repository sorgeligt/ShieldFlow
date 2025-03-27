package com.tmdb.movie

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import com.sorgeligt.shieldflow.Telemetry
import com.sorgeligt.shieldflow.network.NetworkTelemetry
import com.tmdb.movie.network.ApiService
import dagger.hilt.android.HiltAndroidApp
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

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
                .shieldflowNetworkEventsAdapter(
                    shieldflowNetworkEventsAdapter
                )
                .build()
        )
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
