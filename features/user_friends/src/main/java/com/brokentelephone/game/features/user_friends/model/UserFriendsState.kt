package com.brokentelephone.game.features.user_friends.model

import com.brokentelephone.game.core.model.user.AddFriendUserUi
import com.brokentelephone.game.core.model.user.UserUi

data class UserFriendsState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val filteredFriends: List<AddFriendUserUi> = emptyList(),
    val searchQuery: String = "",
    val user: UserUi? = null,
    val globalError: String? = null,
    val removeFriendDialogUserId: String? = null,
    val isRemoveFriendLoading: Boolean = false,
    val addingFriendUserIds: Set<String> = emptySet(),
    val cancelRequestDialogUserId: String? = null,
    val isCancelRequestLoading: Boolean = false,
    val acceptingUserIds: Set<String> = emptySet(),
    val suggestedUsers: List<AddFriendUserUi> = emptyList(),
    val sendingRequestUserIds: Set<String> = emptySet(),
)
