package com.brokentelephone.game.features.edit_email.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.essentials.exceptions.auth.EmailAlreadyInUseException
import kotlinx.coroutines.Dispatchers

class SendEmailChangeVerificationUseCase(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(newEmail: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            val existingUser = usersRepository.getUserByEmail(newEmail)
            if (existingUser != null) throw EmailAlreadyInUseException()
            authRepository.sendEmailChangeVerification(newEmail)
        }
    }
}
