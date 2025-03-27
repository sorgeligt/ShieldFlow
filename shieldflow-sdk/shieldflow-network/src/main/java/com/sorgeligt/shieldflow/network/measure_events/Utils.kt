package com.sorgeligt.shieldflow.network.measure_events

import okhttp3.Response

private val ID_REGEX = "/[0-9]+/".toRegex()
private val UUID_REGEX =
    "/[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}/".toRegex()
private val UUID_REGEX_2 =
    "/[a-fA-F0-9]{32}/".toRegex()
private const val ID_TEMPLATE = "/{id}/"
private const val UUID_TEMPLATE = "/{uuid}/"

internal fun Response.extractXRequestId(): String? = header("X-Request-Id")?.let { headerValue ->
    headerValue.split(",")
        .map { it.trim().lowercase() }
        .toSet()
        .joinToString(", ")
        .takeIf { it.isNotEmpty() }
}

// replace all id-kind and uuid-kind segments
internal fun cleanAndFormatCallName(path: String): String = "${path}/"
    .replace(ID_REGEX, ID_TEMPLATE)
    .replace(ID_REGEX, ID_TEMPLATE)
    .replace(UUID_REGEX, UUID_TEMPLATE)
    .replace(UUID_REGEX, UUID_TEMPLATE)
    .replace(UUID_REGEX_2, UUID_TEMPLATE)
    .replace(UUID_REGEX_2, UUID_TEMPLATE)
    .dropLast(1)
