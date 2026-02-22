package com.broken.telephone.features.account_settings.use_case

import com.broken.telephone.domain.user.UserSession

class DeleteAccountUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke() = userSession.deleteAccount()
}
