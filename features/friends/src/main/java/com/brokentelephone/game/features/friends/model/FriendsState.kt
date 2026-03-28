package com.brokentelephone.game.features.friends.model

import com.brokentelephone.game.core.model.user.UserUi

data class FriendsState(
    val filteredFriends: List<UserUi> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val loadError: String? = null,
    val globalError: String? = null,
)
