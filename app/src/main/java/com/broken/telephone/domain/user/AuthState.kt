package com.broken.telephone.domain.user

sealed class AuthState {
    data object NotAuth : AuthState()
    data class Auth(val user: User) : AuthState()
    data class Guest(val user: User) : AuthState()

    fun getUserOrNull(): User? {
        return when(this) {
            is Auth -> user
            is Guest -> user
            NotAuth -> null
        }
    }
}
