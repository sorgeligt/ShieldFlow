package com.sorgeligt.shieldflow.network.measure_events

import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.Response

internal class NotifyOnResponseInterceptor(
    private val measureEventListener: MeasureEventListener
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request()).also { notifyOnResponse(chain.call(), it) }

    private fun notifyOnResponse(call: Call, response: Response) {
        measureEventListener.onResponse(call, response)
    }
}
