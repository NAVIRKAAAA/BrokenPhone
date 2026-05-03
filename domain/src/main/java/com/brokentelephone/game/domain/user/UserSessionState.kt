package com.brokentelephone.game.domain.user

data class UserSessionState(
    val authState: AuthState,
    val user: User?
) {

    companion object {
        val Initial = UserSessionState(
            authState = AuthState.Loading,
            user = null
        )

        val NotAuth = UserSessionState(
            authState = AuthState.NotAuth,
            user = null
        )
    }

}
