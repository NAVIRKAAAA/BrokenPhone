package com.broken.telephone.features.edit_avatar.use_case

import com.broken.telephone.domain.user.UserSession

class UpdateAvatarUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke(avatarUrl: String) {
        userSession.updateAvatar(avatarUrl)
    }
}
