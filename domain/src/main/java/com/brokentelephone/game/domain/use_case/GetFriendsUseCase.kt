package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.repository.FriendsRepository
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetFriendsUseCase(
    private val userSession: UserSession,
    private val friendsRepository: FriendsRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<List<User>> {
        return handler.handle(Dispatchers.IO) {
            val userId = userSession.getUserId() ?: throw UnauthorizedException()
            
            friendsRepository.getFriends(userId)
        }
    }

    // TODO: Improve getFriendshipActionState for each user
    suspend fun execute(userId: String): AppResult<List<Pair<User, FriendshipActionState>>> {
        return handler.handle(Dispatchers.IO) {
            val currentUserId = userSession.getUserId() ?: throw UnauthorizedException()
            
            val friends = friendsRepository.getFriends(userId)
            coroutineScope {
                friends.map { friend ->
                    async {
                        val state = try {
                            friendsRepository.getFriendshipActionState(currentUserId, friend.id)
                        } catch (e: Exception) {
                            FriendshipActionState.NOT_FRIENDS
                        }
                        friend to state
                    }
                }.awaitAll()
            }
        }
    }
}
