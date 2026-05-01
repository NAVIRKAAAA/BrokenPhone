package com.brokentelephone.game.features.new_password.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class UpdatePasswordUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(newPassword: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.updatePassword(newPassword)
            userSession.signOut()
        }
    }
}
