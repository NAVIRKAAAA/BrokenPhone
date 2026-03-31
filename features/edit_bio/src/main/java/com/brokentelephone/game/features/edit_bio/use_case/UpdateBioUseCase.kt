package com.brokentelephone.game.features.edit_bio.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class UpdateBioUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(bio: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.updateBio(bio)
        }
    }
}
