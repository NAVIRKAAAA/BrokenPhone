package com.brokentelephone.game.features.account_settings.use_case

import com.brokentelephone.game.domain.user.UserSession

class DeleteAccountUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke() = userSession.deleteAccount()
}
