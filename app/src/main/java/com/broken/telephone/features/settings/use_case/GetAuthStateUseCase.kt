package com.broken.telephone.features.settings.use_case

import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.flow.Flow

class GetAuthStateUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<AuthState> = userSession.authState
}
