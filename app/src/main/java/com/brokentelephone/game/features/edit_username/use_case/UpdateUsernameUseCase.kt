package com.brokentelephone.game.features.edit_username.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class UpdateUsernameUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler
) {
    suspend fun execute(username: String) : AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.updateUsername(username)
        }
    }
}
