package com.brokentelephone.game.features.user_details.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.repository.FriendsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers

class GetFriendshipActionStateUseCase(
    private val friendsRepository: FriendsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(
        targetUserId: String
    ): AppResult<FriendshipActionState> {
        return handler.handle(Dispatchers.IO) {
            val userId = userSession.getUserId() ?: throw UnauthorizedException()

            if (userId == targetUserId) {
                FriendshipActionState.IS_ME
            } else {
                friendsRepository.getFriendshipActionState(userId, targetUserId)
            }
        }
    }
}
