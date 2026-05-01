package com.brokentelephone.game.features.blocked_users.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.features.blocked_users.model.BlockedUserUi
import com.brokentelephone.game.features.blocked_users.model.toUi
import kotlinx.coroutines.Dispatchers

class GetBlockedUsersUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {

    suspend fun execute(): AppResult<List<BlockedUserUi>> {
        return handler.handle(Dispatchers.IO) {
            userSession.getBlockedUsers().map { it.toUi() }
        }
    }
}
