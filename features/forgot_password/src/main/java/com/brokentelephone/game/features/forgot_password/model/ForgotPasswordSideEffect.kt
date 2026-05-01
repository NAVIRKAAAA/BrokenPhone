package com.brokentelephone.game.features.forgot_password.model

sealed interface ForgotPasswordSideEffect {
    data object ClearFocus : ForgotPasswordSideEffect
    data object NavigateBack : ForgotPasswordSideEffect
}
