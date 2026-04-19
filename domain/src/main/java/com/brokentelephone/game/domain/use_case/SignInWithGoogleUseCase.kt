package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.google.GoogleSignInManager
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import kotlinx.coroutines.Dispatchers

class SignInWithGoogleUseCase(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val googleSignInManager: GoogleSignInManager,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<Boolean> {

        val idToken = try {
            googleSignInManager.getIdToken()
        } catch (e: Exception) {
            return AppResult.Error(e)
        }

        return handler.handle(Dispatchers.IO) {
            val result = authRepository.signInWithGoogle(idToken)
            if (result.isNewUser) {
                usersRepository.createUser(
                    id = result.uid,
                    email = result.email,
                )
            }
            result.isNewUser
        }
    }
}
