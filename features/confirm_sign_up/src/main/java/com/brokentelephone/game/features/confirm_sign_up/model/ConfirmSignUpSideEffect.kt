package com.brokentelephone.game.features.confirm_sign_up.model

sealed interface ConfirmSignUpSideEffect {
    data object EmailVerified : ConfirmSignUpSideEffect
}
