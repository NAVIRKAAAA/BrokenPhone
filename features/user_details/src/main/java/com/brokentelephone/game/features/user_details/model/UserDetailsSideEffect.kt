package com.brokentelephone.game.features.user_details.model

sealed class UserDetailsSideEffect {
    data object NavigateBack : UserDetailsSideEffect()
}
