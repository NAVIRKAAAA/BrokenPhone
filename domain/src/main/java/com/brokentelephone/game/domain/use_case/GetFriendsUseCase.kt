package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.repository.FriendsRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import com.brokentelephone.game.essentials.exceptions.auth.UserNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull

class GetFriendsUseCase(
    private val userSession: UserSession,
    private val usersRepository: UsersRepository,
    private val friendsRepository: FriendsRepository,
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

    suspend fun execute(userId: String): AppResult<List<Pair<User, FriendshipActionState>>> {
        return handler.handle(Dispatchers.IO) {
            val user = usersRepository.getUserById(userId) ?: throw UserNotFoundException()

            val currentUser =
                userSession.authState.firstOrNull()?.getUserOrNull()
                    ?: throw UnauthorizedException()
            val users = usersRepository.getUsersByIds(user.friendIds)

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
