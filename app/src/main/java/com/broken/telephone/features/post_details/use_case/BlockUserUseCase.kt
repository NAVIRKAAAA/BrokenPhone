package com.broken.telephone.features.post_details.use_case

import com.broken.telephone.domain.user.UserSession

class BlockUserUseCase(
    private val userSession: UserSession,
) {

    suspend operator fun invoke(blockedUserId: String) {
        userSession.blockUser(blockedUserId)
    }

}
