package com.broken.telephone.domain.auth

sealed interface SignUpResult {
    data object Success : SignUpResult

    sealed interface Error : SignUpResult {
        data object InvalidEmail : Error
        data object PasswordTooShort : Error
        data object PasswordsDoNotMatch : Error
    }
}
