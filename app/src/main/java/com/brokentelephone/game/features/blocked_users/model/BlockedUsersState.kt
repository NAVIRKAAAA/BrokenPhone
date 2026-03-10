package com.brokentelephone.game.features.blocked_users.model

data class BlockedUsersState(
    val blockedUsers: List<BlockedUserUi> = emptyList(),
    val unblockDialogUser: BlockedUserUi? = null,
    val isUnblockLoading: Boolean = false,
)
