package com.brokentelephone.game.features.add_friend.model

import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.domain.model.friend.FriendshipActionState

data class AddFriendUserUi(
    val user: UserUi,
    val friendshipState: FriendshipActionState?,
    val isActionLoading: Boolean = false,
)
