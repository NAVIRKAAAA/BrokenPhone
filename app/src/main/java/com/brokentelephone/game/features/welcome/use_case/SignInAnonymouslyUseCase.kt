package com.brokentelephone.game.features.welcome.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class SignInAnonymouslyUseCase(
    private val authRepository: AuthRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<String> {
        return handler.handle(Dispatchers.IO) {
            authRepository.signInAnonymously()
        }
    }
}
