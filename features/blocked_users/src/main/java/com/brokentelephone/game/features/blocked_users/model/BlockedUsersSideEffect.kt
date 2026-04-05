package com.brokentelephone.game.features.blocked_users.model

sealed interface BlockedUsersSideEffect {
    data object NavigateBack : BlockedUsersSideEffect
}
