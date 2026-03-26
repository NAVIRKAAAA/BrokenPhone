package com.brokentelephone.game.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NetworkConnectionManager(
    context: Context,
) : ConnectivityManager.NetworkCallback() {

    private var isConnected = false
    private var currentNetwork: NetworkModel? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    private val networkRequest: NetworkRequest by lazy {
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }

    private val connectivityManager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    private val _network: MutableStateFlow<NetworkModel?> = MutableStateFlow(null)

    init {
        registerNetworkCallback()
    }

    @OptIn(FlowPreview::class)
    val networkState: StateFlow<Boolean> = _network
        .debounce(NETWORK_STATE_DELAY)
        .map { state ->
            val connected = (state != null && state.networkState == NetworkState.CONNECTED)
            connected || isConnected()
        }
        .stateIn(
            scope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    private fun registerNetworkCallback() {
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(this)
    }

    fun hasInternet(): Boolean {
        connectivityManager.activeNetwork?.let {
            val capabilities = connectivityManager.getNetworkCapabilities(it)
            val isValidated = capabilities?.hasCapability(NET_CAPABILITY_VALIDATED)
            return isValidated == true
        }
        return false
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        isConnected = true
        currentNetwork = NetworkModel(
            networkID = network.hashCode(),
            networkState = NetworkState.CONNECTED,
            networkType = NetworkType.UNRECOGNIZED
        )
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        isConnected = false
        currentNetwork?.let {
            if (network.hashCode() == it.networkID) {
                onNetwork(it.copy(networkState = NetworkState.LOST))
            } else {
                onNetwork(getLostNetworkModel(network))
            }
        } ?: run {
            onNetwork(getLostNetworkModel(network))
        }
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        onNetwork(currentNetwork?.copy(networkType = getNetworkType(networkCapabilities)))
    }

    private fun onNetwork(network: NetworkModel?) {
        _network.value = network
        currentNetwork = network
    }

    private fun getNetworkType(networkCapabilities: NetworkCapabilities) = when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
        else -> NetworkType.UNRECOGNIZED
    }

    private fun isConnected(): Boolean = isConnected or hasInternet()

    private fun getLostNetworkModel(network: Network) = NetworkModel(
        networkID = network.hashCode(),
        networkState = NetworkState.LOST,
        networkType = NetworkType.UNRECOGNIZED
    )

    private companion object {
        const val NETWORK_STATE_DELAY = 500L
    }
}
