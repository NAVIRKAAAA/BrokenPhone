package com.brokentelephone.game.domain.auth

sealed interface SignUpResult {
    data object Success : SignUpResult

    sealed interface Error : SignUpResult {
        data object InvalidEmail : Error
        data object PasswordTooShort : Error
        data object PasswordsDoNotMatch : Error
    }
}
