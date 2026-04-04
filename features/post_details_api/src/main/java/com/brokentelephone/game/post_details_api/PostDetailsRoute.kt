package com.brokentelephone.game.post_details_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class PostDetailsRoute(val postId: String) : NavigationRoute()