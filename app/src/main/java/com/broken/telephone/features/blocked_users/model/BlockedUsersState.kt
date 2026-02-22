package com.broken.telephone.features.blocked_users.model

data class BlockedUsersState(
    val blockedUsers: List<BlockedUserUi> = emptyList(),
    val unblockDialogUser: BlockedUserUi? = null,
    val isUnblockLoading: Boolean = false,
)
