package com.brokentelephone.game.chain_details_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class ChainDetailsRoute(val postId: String, val userId: String = "") : NavigationRoute()