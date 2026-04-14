package com.brokentelephone.game.features.sign_up.use_case

import android.util.Log
import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import kotlinx.coroutines.Dispatchers

class SignUpUseCase(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(
        email: String,
        password: String,
    ): AppResult<Unit> {
        return handler.handle(Dispatchers.IO, maxRetries = 0) {
            val userId = authRepository.signUpWithEmailPassword(email, password)
            Log.d("LOG_TAG", "SignUpUseCase: $userId")
//            usersRepository.createUser(id = userId, email = email, authProvider = AuthProvider.EMAIL)
        }
    }
}
