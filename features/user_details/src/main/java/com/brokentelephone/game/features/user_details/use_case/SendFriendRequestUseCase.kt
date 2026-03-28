package com.brokentelephone.game.features.user_details.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.FriendsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class SendFriendRequestUseCase(
    private val friendsRepository: FriendsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(targetUserId: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO, maxRetries = 0) {
            val user = userSession.authState.first().getUserOrNull() ?: throw UnauthorizedException()
            friendsRepository.sendFriendRequest(user.id, targetUserId)
        }
    }
}
