package com.brokentelephone.game.features.settings.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class GetBlockedUsersCountUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<Int> =
        handler.handle(Dispatchers.IO) { userSession.getBlockedUsers().size }
}
