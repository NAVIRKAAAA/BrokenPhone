package com.brokentelephone.game.features.edit_username.use_case

import com.brokentelephone.game.domain.user.UserSession

class UpdateProfileUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke(username: String) {
        userSession.updateProfile(username)
    }
}
