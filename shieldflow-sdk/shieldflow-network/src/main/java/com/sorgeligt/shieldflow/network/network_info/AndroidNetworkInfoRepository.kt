package com.sorgeligt.shieldflow.network.network_info

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.net.ProxySelector

internal class AndroidNetworkInfoRepository(
    private val connectivityManager: ConnectivityManager,
) : NetworkInfoRepository {

    @Volatile
    private var networkInfoCache: NetworkInfo? = null

    override fun getCurrentNetworkInfo(): NetworkInfo =
        networkInfoCache ?: connectivityManager.activeNetwork
            .let(connectivityManager::getNetworkCapabilities)
            .let(::updateNetworkInfo)

    private fun updateNetworkInfo(capabilities: NetworkCapabilities?): NetworkInfo {
        ProxySelector.getDefault()
        NetworkCapabilities.TRANSPORT_VPN
            return NetworkInfo(
                isCellular = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
                isVpn = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN),
            ).also { networkInfoCache = it }
        }
}
