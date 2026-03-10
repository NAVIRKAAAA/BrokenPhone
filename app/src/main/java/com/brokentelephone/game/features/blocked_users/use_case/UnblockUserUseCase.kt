package com.brokentelephone.game.features.blocked_users.use_case

import com.brokentelephone.game.domain.user.UserSession

class UnblockUserUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke(blockId: String) = userSession.unblockUser(blockId)
}
