package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.google.GoogleSignInManager
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class LogoutUseCase(
    private val userSession: UserSession,
    private val googleSignInManager: GoogleSignInManager,
    private val handler: ApiHandler
) {
    suspend fun execute(): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.signOut()
            googleSignInManager.clearCredentialState()
        }
    }
}
