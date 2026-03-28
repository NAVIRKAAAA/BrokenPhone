package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class GetFriendsUseCase(
    private val userSession: UserSession,
    private val usersRepository: UsersRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<List<User>> {
        return handler.handle(Dispatchers.IO) {
            val friendIds =
                userSession.authState.firstOrNull()?.getUserOrNull()?.friendIds
                    ?: throw UnauthorizedException()

            usersRepository.getUsersByIds(friendIds)
        }
    }
}
