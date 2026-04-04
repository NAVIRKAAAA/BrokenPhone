package com.brokentelephone.game.features.welcome.use_case

import com.brokentelephone.game.core.avatar.Avatars
import com.brokentelephone.game.core.username.SuggestedUsernames
import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import kotlinx.coroutines.Dispatchers

class SignInAnonymouslyUseCase(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            val userId = authRepository.signInAnonymously()
            val username = SuggestedUsernames.random()
            val avatarUrl = Avatars.all.random().url

            usersRepository.createUser(userId, username, avatarUrl)
        }
    }
}
