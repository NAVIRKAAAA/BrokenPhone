package com.brokentelephone.game.features.blocked_users.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class UnblockUserUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler
) {
    suspend fun execute(blockId: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.unblockUser(blockId)
        }
    }
}
