package com.sorgeligt.shieldflow.network.measure_events

import okhttp3.Call
import okhttp3.Response
import java.util.concurrent.ConcurrentHashMap

internal class CallPayload(
    val call: Call,
    val startQueue: Int,
    val startTimeMs: Long,
    val stages: ConcurrentHashMap<String, Long> = ConcurrentHashMap(),
    @Volatile var response: Response? = null,
    @Volatile var xRequestId: String? = null,
    @Volatile var operationName: String? = null,
    @Volatile var dnsStartTimeMs: Long? = null,
    @Volatile var connectStartTimeMs: Long? = null,
    @Volatile var secConnectStartTimeMs: Long? = null,
    @Volatile var reqStartTimeMs: Long? = null,
    @Volatile var reqHeadersStartTimeMs: Long? = null,
    @Volatile var reqBodyStartTimeMs: Long? = null,
    @Volatile var respStartTimeMs: Long? = null,
    @Volatile var respHeadersStartTimeMs: Long? = null,
    @Volatile var respBodyStartTimeMs: Long? = null,
) {

    companion object {
        const val DNS_LOOKUP_STAGE = "DNS lookup"
        const val CONNECTION_STAGE = "establishing connection"
        const val SECURE_CONNECTION_STAGE = "secure connection"
        const val REQUEST_STAGE = "request"
        const val RESPONSE_STAGE = "response"
        const val RESPONSE_BODY_STAGE = "response body"
        const val RESPONSE_HEADERS_STAGE = "response headers"
        const val REQUEST_BODY_STAGE = "request body"
        const val REQUEST_HEADERS_STAGE = "request headers"
    }
}

internal sealed interface CallStatus {
    val name: String

    object Success : CallStatus {
        override val name: String = "success"
    }

    object Cancelled : CallStatus {
        override val name: String = "cancelled"
    }

    data class Failure(
        val exception: Exception,
    ) : CallStatus {
        override val name: String = "failure"

        companion object {

            fun Failure.toCallStatusPayload(): Map<String, String> {
                val payload = mutableMapOf<String, String>()
                payload["class_name"] = exception.javaClass.name
                exception.message?.let { payload["description"] = it }
                return payload
            }
        }
    }
}

