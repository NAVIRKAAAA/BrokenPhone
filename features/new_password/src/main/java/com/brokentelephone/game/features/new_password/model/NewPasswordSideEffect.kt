package com.brokentelephone.game.features.new_password.model

sealed interface NewPasswordSideEffect {
    data object NavigateBack : NewPasswordSideEffect
    data class NavigateToSignIn(val email: String) : NewPasswordSideEffect
}
