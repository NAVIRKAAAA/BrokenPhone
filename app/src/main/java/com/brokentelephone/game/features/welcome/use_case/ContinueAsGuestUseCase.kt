package com.brokentelephone.game.features.welcome.use_case

import com.brokentelephone.game.domain.user.UserSession

class ContinueAsGuestUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke() {
        return
    }
}
