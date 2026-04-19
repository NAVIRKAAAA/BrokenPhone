package com.brokentelephone.game.features.sign_up.model

sealed interface SignUpSideEffect {
    data object SignedUp : SignUpSideEffect
    data object NavigateToChooseAvatar : SignUpSideEffect
    data class NavigateToConfirmSignUp(val email: String) : SignUpSideEffect
    data object ClearFocus : SignUpSideEffect
    data class OpenLink(val url: String) : SignUpSideEffect
}
