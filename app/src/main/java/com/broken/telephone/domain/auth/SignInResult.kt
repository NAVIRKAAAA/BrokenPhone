package com.broken.telephone.domain.auth

sealed interface SignInResult {
    data object Success : SignInResult

    sealed interface Error : SignInResult {
        data object InvalidCredentials : Error
    }
}
