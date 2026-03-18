package com.brokentelephone.game.features.sign_up.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.AuthProvider
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
        return handler.handle(Dispatchers.IO) {
            val userId = authRepository.signUpWithEmailPassword(email, password)
            usersRepository.createUser(id = userId, email = email, authProvider = AuthProvider.EMAIL)
        }
    }
}
