package com.brokentelephone.game.features.add_friend.model

import com.brokentelephone.game.core.model.user.AddFriendUserUi

data class AddFriendState(
    val searchQuery: String = "",
    val results: List<AddFriendUserUi> = emptyList(),
    val isSearching: Boolean = false,
    val isLoading: Boolean = true,
    val pendingInvites: List<AddFriendUserUi> = emptyList(),
    val receivedPendingInvites: List<AddFriendUserUi> = emptyList(),
    val globalError: String? = null,
    val isRefreshing: Boolean = false,
    val addingFriendUserIds: Set<String> = emptySet(),
    val acceptingUserIds: Set<String> = emptySet(),
    val decliningUserIds: Set<String> = emptySet(),
    val cancelRequestDialogUserId: String? = null,
    val isCancelRequestLoading: Boolean = false,
    val removeFriendDialogUserId: String? = null,
    val isRemoveFriendLoading: Boolean = false,
)
