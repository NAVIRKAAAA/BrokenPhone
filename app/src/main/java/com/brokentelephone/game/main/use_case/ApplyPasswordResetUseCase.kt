package com.brokentelephone.game.main.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class ApplyPasswordResetUseCase(
    private val authRepository: AuthRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(code: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            authRepository.importSession(code)
        }
    }
}
