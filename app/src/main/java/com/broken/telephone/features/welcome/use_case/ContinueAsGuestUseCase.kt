package com.broken.telephone.features.welcome.use_case

import com.broken.telephone.domain.user.UserSession

class ContinueAsGuestUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke() = userSession.setupGuest()
}
