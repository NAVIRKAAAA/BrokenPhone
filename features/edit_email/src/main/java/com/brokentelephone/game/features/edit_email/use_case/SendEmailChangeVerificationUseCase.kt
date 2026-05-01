package com.brokentelephone.game.features.edit_email.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class SendEmailChangeVerificationUseCase(
    private val authRepository: AuthRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(newEmail: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            authRepository.sendEmailChangeVerification(newEmail)
        }
    }
}
