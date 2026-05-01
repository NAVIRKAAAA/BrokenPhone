package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.repository.UserSettingsRepository

class SetPendingEmailUseCase(
    private val userSettingsRepository: UserSettingsRepository,
) {
    suspend fun execute(email: String) {
        userSettingsRepository.setPendingEmail(email)
    }
}
