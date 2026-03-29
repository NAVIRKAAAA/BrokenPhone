package com.brokentelephone.game.features.add_friend.model

data class AddFriendState(
    val searchQuery: String = "",
    val results: List<AddFriendUserUi> = emptyList(),
    val isSearching: Boolean = false,
    val isLoading: Boolean = true,
    val pendingInvites: List<AddFriendUserUi> = emptyList(),
    val receivedPendingInvites: List<AddFriendUserUi> = emptyList(),
    val globalError: String? = null,
    val isRefreshing: Boolean = false
)
