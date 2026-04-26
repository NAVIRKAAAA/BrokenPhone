package com.brokentelephone.game.domain.user

sealed class AuthState {
    data object Loading : AuthState()
    data object NotAuth : AuthState()
    data class PreAuth(val userId: String) : AuthState()
    data class Auth(val user: User) : AuthState()
    data class Guest(val user: User) : AuthState()

    fun getUserOrNull(): User? {
        return when (this) {
            is Auth -> user
            is Guest -> user
            else -> null
        }
    }

    fun isAuth(): Boolean {
        return this is Auth
    }
}
