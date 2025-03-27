package com.sorgeligt.shieldflow.core.analyst;

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

internal interface LogApi {
    @POST("/log")
    fun sendLog(@Body body: LogEvent): Call<Unit>
}

internal data class LogEvent(
    val event: String,
    val params: Map<String, Any?>?
)
