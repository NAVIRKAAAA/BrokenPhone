package com.brokentelephone.game.features.confirm_sign_up.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class VerifyEmailOtpUseCase(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {

    // TODO: handle usersRepository.createUser(id, email) with Supabase trigger?
    suspend fun execute(email: String, otp: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            authRepository.verifyEmailOtp(email, otp)
            val id = authRepository.getCurrentUserId()
            usersRepository.createUser(id, email)
            userSession.refreshFcmToken()
        }
    }
}
