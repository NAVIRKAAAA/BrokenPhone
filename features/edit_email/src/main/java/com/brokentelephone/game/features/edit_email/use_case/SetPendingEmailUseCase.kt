package com.brokentelephone.game.features.edit_email.use_case

import com.brokentelephone.game.domain.repository.UserSettingsRepository

class SetPendingEmailUseCase(
    private val userSettingsRepository: UserSettingsRepository,
) {
    suspend fun execute(email: String) {
        userSettingsRepository.setPendingEmail(email)
    }
}
