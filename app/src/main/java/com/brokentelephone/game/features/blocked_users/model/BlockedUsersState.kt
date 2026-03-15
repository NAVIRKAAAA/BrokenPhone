package com.brokentelephone.game.features.blocked_users.model

data class BlockedUsersState(
    val blockedUsers: List<BlockedUserUi> = emptyList(),
    val isLoading: Boolean = true,
    val unblockDialogUser: BlockedUserUi? = null,
    val isUnblockLoading: Boolean = false,
    val globalError: String? = null,
    val loadError: String? = null,
    val isLoadRetrying: Boolean = false,
    val isRefreshing: Boolean = false
)
