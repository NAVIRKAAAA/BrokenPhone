package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.first

class GetPendingEmailUseCase(
    private val userSettingsRepository: UserSettingsRepository,
) {
    suspend fun execute(): String? = userSettingsRepository.getPendingEmail().first()
}
