package com.brokentelephone.game.main.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class ApplyEmailChangeUseCase(
    private val authRepository: AuthRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(oobCode: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            authRepository.applyEmailChange(oobCode)

            userSession.signOut()
        }
    }
}
