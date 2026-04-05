package com.brokentelephone.game.features.account_settings.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class SendEmailVerificationUseCase(
    private val authRepository: AuthRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(email: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            authRepository.sendEmailVerification(email)
        }
    }
}
