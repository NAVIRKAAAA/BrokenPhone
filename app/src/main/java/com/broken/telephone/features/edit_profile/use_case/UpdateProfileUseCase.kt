package com.broken.telephone.features.edit_profile.use_case

import com.broken.telephone.domain.user.UserSession

class UpdateProfileUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke(username: String) {
        userSession.updateProfile(username)
    }
}
