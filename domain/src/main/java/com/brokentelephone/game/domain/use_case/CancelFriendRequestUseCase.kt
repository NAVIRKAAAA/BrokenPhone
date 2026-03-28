package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.FriendsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import com.brokentelephone.game.essentials.exceptions.firestore.FirestoreNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class CancelFriendRequestUseCase(
    private val friendsRepository: FriendsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(targetUserId: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO, maxRetries = 0) {
            val user = userSession.authState.first().getUserOrNull() ?: throw UnauthorizedException()
            val requestId = friendsRepository.getSentPendingRequestId(user.id, targetUserId)
                ?: throw FirestoreNotFoundException()
            friendsRepository.cancelFriendRequest(requestId)
        }
    }
}
