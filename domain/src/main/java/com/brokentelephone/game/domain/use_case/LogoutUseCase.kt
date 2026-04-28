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

    // TODO: clearCredentialState if isGoogleUser

    suspend fun execute(): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
//            val isGoogleUser = userSession.authState.first()
//                .getUserOrNull()?.authProvider == AuthProvider.GOOGLE

            userSession.deleteFcmToken()
            userSession.signOut()

//            if (isGoogleUser) {
                googleSignInManager.clearCredentialState()
//            }
        }
    }
}
