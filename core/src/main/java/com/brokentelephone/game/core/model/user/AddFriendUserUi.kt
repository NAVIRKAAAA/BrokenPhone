package com.brokentelephone.game.core.model.user

import com.brokentelephone.game.domain.model.friend.FriendshipActionState

data class AddFriendUserUi(
    val user: UserUi,
    val friendshipState: FriendshipActionState?,
    val isActionLoading: Boolean = false,
)
