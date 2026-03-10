package com.brokentelephone.game.features.sign_in.model

sealed interface SignInSideEffect {
    data object SignedIn : SignInSideEffect
    data object ClearFocus : SignInSideEffect
}
