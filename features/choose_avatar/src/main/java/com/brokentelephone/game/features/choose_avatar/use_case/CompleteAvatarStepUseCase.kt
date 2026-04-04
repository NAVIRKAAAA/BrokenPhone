package com.brokentelephone.game.features.choose_avatar.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class CompleteAvatarStepUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(avatarUrl: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.completeAvatarStep(avatarUrl)
        }
    }
}
