package com.broken.telephone.domain.user

sealed class AuthState {
    data object NotAuth : AuthState()
    data class Auth(val user: User) : AuthState()
    data object Guest : AuthState()
}
