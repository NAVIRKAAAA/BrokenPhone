package com.broken.telephone.features.settings.use_case

import com.broken.telephone.domain.user.UserSession

class LogoutUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke() = userSession.logout()
}
