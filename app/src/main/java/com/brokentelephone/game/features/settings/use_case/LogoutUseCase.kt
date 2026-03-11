package com.brokentelephone.game.features.settings.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class LogoutUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler
) {
    suspend fun execute(): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.signOut()
        }
    }
}
