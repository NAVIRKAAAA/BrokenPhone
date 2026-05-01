package com.brokentelephone.game.features.friends.model

import com.brokentelephone.game.core.model.user.AddFriendUserUi
import com.brokentelephone.game.core.model.user.UserUi

data class FriendsState(
    val suggestedUsers: List<AddFriendUserUi> = emptyList(),
    val filteredFriends: List<UserUi> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val globalError: String? = null,
    val selectedFriendId: String? = null,
    val isRemoveFriendLoading: Boolean = false,
    val acceptingUserIds: Set<String> = emptySet(),
    val sendingRequestUserIds: Set<String> = emptySet(),
    val cancelRequestDialogUserId: String? = null,
    val isCancelRequestLoading: Boolean = false,
)
