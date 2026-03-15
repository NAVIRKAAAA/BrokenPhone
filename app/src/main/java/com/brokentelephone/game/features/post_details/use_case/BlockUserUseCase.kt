package com.brokentelephone.game.features.post_details.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class BlockUserUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler
) {

    suspend fun execute(userId: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.blockUser(userId)
        }
    }

}
