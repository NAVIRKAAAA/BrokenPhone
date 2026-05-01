package com.brokentelephone.game.domain.user

sealed class AuthState {
    data object Loading : AuthState()
    data object NotAuth : AuthState()

    data class PreAuth(val userId: String) : AuthState()
    data class Auth(val user: User) : AuthState()

    fun getUserIdOrNull(): String? {
        return when (this) {
            is PreAuth -> userId
            is Auth -> user.id
            else -> null
        }
    }

    fun getUserOrNull(): User? {
        return when (this) {
            is Auth -> user
            else -> null
        }
    }

    fun isAuth(): Boolean {
        return this is Auth
    }
}
