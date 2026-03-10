package com.brokentelephone.game.features.post_details.use_case

import com.brokentelephone.game.domain.user.UserSession

class BlockUserUseCase(
    private val userSession: UserSession,
) {

    suspend operator fun invoke(blockedUserId: String) {
        userSession.blockUser(blockedUserId)
    }

}
