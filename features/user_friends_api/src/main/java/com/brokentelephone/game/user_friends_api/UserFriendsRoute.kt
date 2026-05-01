package com.brokentelephone.game.user_friends_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class UserFriendsRoute(val userId: String) : NavigationRoute()
