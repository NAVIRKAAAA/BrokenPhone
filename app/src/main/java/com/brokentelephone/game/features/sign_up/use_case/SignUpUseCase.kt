package com.brokentelephone.game.features.sign_up.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class SignUpUseCase(
    private val authRepository: AuthRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(
        email: String,
        password: String,
    ): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            authRepository.signUpWithEmailPassword(email, password)
        }
    }
}
