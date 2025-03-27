package com.sorgeligt.shieldflow.network.network_info

internal interface NetworkInfoRepository {
    fun getCurrentNetworkInfo(): NetworkInfo
}

internal data class NetworkInfo(
    val isCellular: Boolean?,
    val isVpn: Boolean?,
)
