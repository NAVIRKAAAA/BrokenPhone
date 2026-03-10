package com.brokentelephone.game.features.edit_avatar.use_case

import com.brokentelephone.game.domain.user.UserSession

class UpdateAvatarUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke(avatarUrl: String) {
        userSession.updateAvatar(avatarUrl)
    }
}
