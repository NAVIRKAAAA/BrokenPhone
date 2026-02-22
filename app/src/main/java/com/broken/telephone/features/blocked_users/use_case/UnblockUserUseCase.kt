package com.broken.telephone.features.blocked_users.use_case

import com.broken.telephone.domain.user.UserSession

class UnblockUserUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke(blockId: String) = userSession.unblockUser(blockId)
}
