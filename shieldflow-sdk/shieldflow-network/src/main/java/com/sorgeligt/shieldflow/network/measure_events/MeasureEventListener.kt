package com.sorgeligt.shieldflow.network.measure_events

import com.sorgeligt.shieldflow.core.analyst.Analyst
import com.sorgeligt.shieldflow.core.time.TimeProvider
import com.sorgeligt.shieldflow.network.adapters.TelemetryNetworkEventsAdapter
import com.sorgeligt.shieldflow.network.measure_events.CallPayload.Companion.CONNECTION_STAGE
import com.sorgeligt.shieldflow.network.measure_events.CallPayload.Companion.DNS_LOOKUP_STAGE
import com.sorgeligt.shieldflow.network.measure_events.CallPayload.Companion.REQUEST_BODY_STAGE
import com.sorgeligt.shieldflow.network.measure_events.CallPayload.Companion.REQUEST_HEADERS_STAGE
import com.sorgeligt.shieldflow.network.measure_events.CallPayload.Companion.REQUEST_STAGE
import com.sorgeligt.shieldflow.network.measure_events.CallPayload.Companion.RESPONSE_BODY_STAGE
import com.sorgeligt.shieldflow.network.measure_events.CallPayload.Companion.RESPONSE_HEADERS_STAGE
import com.sorgeligt.shieldflow.network.measure_events.CallPayload.Companion.RESPONSE_STAGE
import com.sorgeligt.shieldflow.network.measure_events.CallPayload.Companion.SECURE_CONNECTION_STAGE
import com.sorgeligt.shieldflow.network.measure_events.CallStatus.Failure.Companion.toCallStatusPayload
import com.sorgeligt.shieldflow.network.network_info.NetworkInfoRepository
import com.sorgeligt.shieldflow.network.url_tree.Node
import com.sorgeligt.shieldflow.network.url_tree.addPath
import com.sorgeligt.shieldflow.network.url_tree.findMatchingPattern
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.Handshake
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.ConcurrentHashMap

internal class MeasureEventListener(
    private val analysts: List<Analyst>,
    private val networkInfoRepository: NetworkInfoRepository,
    private val timeProvider: TimeProvider,
    shieldflowNetworkEventsAdapter: TelemetryNetworkEventsAdapter,
) : EventListener() {

    private val root: Node = Node.ConstNode("")
    private val enqueuedCalls: ConcurrentHashMap<Int, CallPayload> = ConcurrentHashMap()

    init {
        shieldflowNetworkEventsAdapter.getPaths().forEach(root::addPath)
    }

    fun onStageFinished(
        call: Call,
        stage: String,
        durationMs: Long
    ) {
        val payload = enqueuedCalls[call.hashCode()] ?: return
        payload.stages += (stage to durationMs)
    }

    fun onResponse(call: Call, response: Response) {
        val payload = enqueuedCalls[call.hashCode()] ?: return
        payload.xRequestId = response.extractXRequestId()
        payload.operationName = call.request().header("x-apollo-operation-name")
        payload.response = response
    }

    override fun callStart(call: Call) {
        if (enqueuedCalls.size >= MAX_REQUESTS_COUNT) {
            enqueuedCalls.iterator().run {
                next()
                remove()
            }
        }
        enqueuedCalls += (call.hashCode() to CallPayload(
            call,
            enqueuedCalls.size,
            timeProvider.nowMillis()
        ))
    }

    override fun callEnd(call: Call) {
        val payload = enqueuedCalls.remove(call.hashCode()) ?: return
        logCallEnded(payload, CallStatus.Success)
    }

    override fun callFailed(call: Call, ioe: IOException) {
        val payload = enqueuedCalls.remove(call.hashCode()) ?: return
        logCallEnded(payload, CallStatus.Failure(ioe))
    }

    override fun canceled(call: Call) {
        val payload = enqueuedCalls.remove(call.hashCode()) ?: return
        logCallEnded(payload, CallStatus.Cancelled)
    }

    override fun dnsStart(call: Call, domainName: String) {
        onLocalStageStarted(call, CallPayload::dnsStartTimeMs::set)
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        onLocalStageFinished(call, DNS_LOOKUP_STAGE, CallPayload::dnsStartTimeMs)
    }

    override fun secureConnectStart(call: Call) {
        onLocalStageStarted(call, CallPayload::secConnectStartTimeMs::set)
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        onLocalStageFinished(call, SECURE_CONNECTION_STAGE, CallPayload::secConnectStartTimeMs)
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        onLocalStageStarted(call, CallPayload::connectStartTimeMs::set)
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        onLocalStageFinished(call, CONNECTION_STAGE, CallPayload::connectStartTimeMs)
    }

    override fun requestHeadersStart(call: Call) {
        onRequestStart(call)
        onLocalStageStarted(call, CallPayload::reqHeadersStartTimeMs::set)
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        onLocalStageFinished(call, REQUEST_STAGE, CallPayload::reqStartTimeMs)
        onLocalStageFinished(call, REQUEST_HEADERS_STAGE, CallPayload::reqHeadersStartTimeMs)
    }

    override fun requestBodyStart(call: Call) {
        onRequestStart(call)
        onLocalStageStarted(call, CallPayload::reqBodyStartTimeMs::set)
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        onLocalStageFinished(call, REQUEST_STAGE, CallPayload::reqStartTimeMs)
        onLocalStageFinished(call, REQUEST_BODY_STAGE, CallPayload::reqBodyStartTimeMs)
    }

    override fun responseHeadersStart(call: Call) {
        onResponseStart(call)
        onLocalStageStarted(call, CallPayload::respHeadersStartTimeMs::set)
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        onLocalStageFinished(call, RESPONSE_STAGE, CallPayload::respStartTimeMs)
        onLocalStageFinished(call, RESPONSE_HEADERS_STAGE, CallPayload::respHeadersStartTimeMs)
    }

    override fun responseBodyStart(call: Call) {
        onResponseStart(call)
        onLocalStageStarted(call, CallPayload::respBodyStartTimeMs::set)
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        onLocalStageFinished(call, RESPONSE_STAGE, CallPayload::respStartTimeMs)
        onLocalStageFinished(call, RESPONSE_BODY_STAGE, CallPayload::respBodyStartTimeMs)
    }

    internal fun addPath(vararg path: String) {
        path.toList().forEach {
            root.addPath(it)
        }
    }

    private fun onRequestStart(call: Call) {
        onLocalStageStarted(call) { startTime ->
            if (reqStartTimeMs == null) reqStartTimeMs = startTime
        }
    }

    private fun onResponseStart(call: Call) {
        onLocalStageStarted(call) { startTime ->
            if (respStartTimeMs == null) respStartTimeMs = startTime
        }
    }

    private fun onLocalStageStarted(call: Call, transformation: CallPayload.(startTime: Long) -> Unit) {
        val payload = enqueuedCalls[call.hashCode()] ?: return
        payload.transformation(timeProvider.nowMillis())
    }

    private fun onLocalStageFinished(
        call: Call,
        stage: String,
        startTimeProvider: CallPayload.() -> Long?
    ) {
        val startTimeMs = enqueuedCalls[call.hashCode()]?.startTimeProvider() ?: return
        onStageFinished(call, stage, timeProvider.nowMillis() - startTimeMs)
    }

    private fun logCallEnded(payload: CallPayload, status: CallStatus) {
        val durationMs = timeProvider.nowMillis() - payload.startTimeMs
        val networkInfo = networkInfoRepository.getCurrentNetworkInfo()
        val encodedPath = payload.call.request().url.encodedPath

        val networkDuration = payload.response?.networkResponse?.let {networkResponse ->
            networkResponse.receivedResponseAtMillis - networkResponse.sentRequestAtMillis
        }

        val params = mutableMapOf<String, Any?>().apply {
            with(payload) {
                put(VERSION_PARAM, CURRENT_VERSION)
                put(HOST_PARAM, call.request().url.host)
                put(
                    ENDPOINT_PARAM,
                    cleanAndFormatCallName(root.findMatchingPattern(encodedPath) ?: encodedPath)
                )
                put(METHOD_PARAM, call.request().method)
                put(DURATION_PARAM, durationMs)
                networkDuration?.let { put(NETWORK_DURATION_PARAM, networkDuration) }
                response?.code?.let { put(HTTP_STATUS_PARAM, it) }
                xRequestId?.let { put(REQUEST_ID_HEADER_PARAM, it) }
                operationName?.let { put(OPERATION_NAME_HEADER_PARAM, it) }
                networkInfo.isCellular?.let { put(IS_CELLULAR_PARAM, it) }
                put(STAGES_PARAM, stages.filter { it.value > 0 })
                networkInfo.isVpn?.let { put(IS_VPN_PARAM, it) }
                put(START_QUEUE_PARAM, startQueue)
                put(FINISH_QUEUE_PARAM, enqueuedCalls.size)
                call.request().body?.contentLength()?.let { length ->
                    if (length >= 0) put(REQ_BODY_SIZE_PARAM, length)
                }
                payload.getResponseContentLength()?.let { put(RES_BODY_SIZE_PARAM, it) }
                STATUS_PARAM to status.name
                (status as? CallStatus.Failure)?.toCallStatusPayload()?.let { put(ERROR_PARAM, it) }
            }
        }
        analysts.forEach { analyst ->
            analyst.log(EVENT_REQUEST, params)
        }
    }

    private fun CallPayload.getResponseContentLength(): Long? {
        val networkResponse = response?.networkResponse
        val responseContentLength = response?.body?.contentLength()
        return if (responseContentLength != null && responseContentLength >= 0) {
            responseContentLength
        } else {
            networkResponse?.header("Content-Length")?.toLongOrNull()
        }
    }

    private companion object {
        private const val EVENT_REQUEST = "network.request_event"

        private const val VERSION_PARAM = "_v"
        private const val HOST_PARAM = "host"
        private const val ENDPOINT_PARAM = "endpoint"
        private const val METHOD_PARAM = "method"
        private const val DURATION_PARAM = "duration"
        private const val NETWORK_DURATION_PARAM = "network_duration"
        private const val HTTP_STATUS_PARAM = "http_status"
        private const val IS_CELLULAR_PARAM = "cellular"
        private const val STATUS_PARAM = "status"
        private const val STAGES_PARAM = "stages"
        private const val REQ_BODY_SIZE_PARAM = "req_body_size"
        private const val RES_BODY_SIZE_PARAM = "res_body_size"
        private const val START_QUEUE_PARAM = "start_queue"
        private const val FINISH_QUEUE_PARAM = "finish_queue"
        private const val IS_VPN_PARAM = "vpn"
        private const val REQUEST_ID_HEADER_PARAM = "request_id"
        private const val OPERATION_NAME_HEADER_PARAM = "operation_name"
        private const val ERROR_PARAM = "error"

        private const val MAX_REQUESTS_COUNT = 20
        private const val CURRENT_VERSION = 2
    }
}

