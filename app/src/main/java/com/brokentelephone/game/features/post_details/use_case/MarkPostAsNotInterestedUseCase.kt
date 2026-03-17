package com.brokentelephone.game.features.post_details.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class MarkPostAsNotInterestedUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {

    suspend fun execute(postParentId: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.markPostAsNotInterested(postParentId)
        }
    }

}
