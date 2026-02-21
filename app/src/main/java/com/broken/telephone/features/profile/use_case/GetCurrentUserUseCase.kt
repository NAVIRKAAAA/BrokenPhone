package com.broken.telephone.features.profile.use_case

import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.UserSession
import com.broken.telephone.features.profile.model.UserUi
import com.broken.telephone.features.profile.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCurrentUserUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<UserUi?> {
        return userSession.authState.map { authState ->
            when (authState) {
                is AuthState.Auth -> authState.user.toUi()
                else -> null
            }
        }
    }
}
