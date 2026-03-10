package com.brokentelephone.game.features.settings.use_case

import com.brokentelephone.game.domain.user.UserSession

class LogoutUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke() = userSession.logout()
}
