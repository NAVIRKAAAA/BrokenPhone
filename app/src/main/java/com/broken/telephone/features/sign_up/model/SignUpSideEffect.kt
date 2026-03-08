package com.broken.telephone.features.sign_up.model

sealed interface SignUpSideEffect {
    data object SignedUp : SignUpSideEffect
    data object ClearFocus : SignUpSideEffect
    data class OpenLink(val url: String) : SignUpSideEffect
}
