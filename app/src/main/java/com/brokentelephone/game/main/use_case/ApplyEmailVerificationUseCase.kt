package com.brokentelephone.game.main.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class ApplyEmailVerificationUseCase(
    private val authRepository: AuthRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(oobCode: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            authRepository.applyEmailVerification(oobCode)
        }
    }
}
