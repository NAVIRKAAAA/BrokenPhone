package com.brokentelephone.game.features.sign_in.model

sealed interface SignInSideEffect {
    data object SignedIn : SignInSideEffect
    data object NavigateToChooseAvatar : SignInSideEffect
    data object ClearFocus : SignInSideEffect
    data class OpenLink(val url: String) : SignInSideEffect
}
