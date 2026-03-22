package com.brokentelephone.game.features.sign_in.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class SignInWithEmailPasswordUseCase(
    private val authRepository: AuthRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(email: String, password: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            authRepository.signInWithEmailPassword(email, password)
        }
    }
}
