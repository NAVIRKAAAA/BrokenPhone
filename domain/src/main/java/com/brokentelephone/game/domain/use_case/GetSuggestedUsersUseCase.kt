package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.repository.FriendsRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull

class GetSuggestedUsersUseCase(
    private val userSession: UserSession,
    private val usersRepository: UsersRepository,
    private val friendsRepository: FriendsRepository,
    private val handler: ApiHandler,
) {
    // TODO: Improve getFriendshipActionState for each user
    suspend fun execute(): AppResult<List<Pair<User, FriendshipActionState>>> {
        return handler.handle(Dispatchers.IO) {
            val currentUser = userSession.authState.firstOrNull()?.getUserOrNull()
                ?: throw UnauthorizedException()

            val users = usersRepository.getSuggestedUsers()

            coroutineScope {
                users.map { user ->
                    async {
                        val friendshipState = try {
                            friendsRepository.getFriendshipActionState(currentUser.id, user.id)
                        } catch (e: Exception) {
                            FriendshipActionState.NOT_FRIENDS
                        }

                        user to friendshipState
                    }
                }.awaitAll()
            }
        }
    }
}
