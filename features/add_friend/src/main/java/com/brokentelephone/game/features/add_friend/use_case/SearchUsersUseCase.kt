package com.brokentelephone.game.features.add_friend.use_case

import com.brokentelephone.game.core.model.user.AddFriendUserUi
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.repository.FriendsRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class SearchUsersUseCase(
    private val usersRepository: UsersRepository,
    private val friendsRepository: FriendsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {

    // TODO: Improve getFriendshipActionState for each user
    suspend fun execute(query: String): AppResult<List<AddFriendUserUi>> {
        return handler.handle(Dispatchers.IO) {
            val currentUser = userSession.authState.first().getUserOrNull()
                ?: throw UnauthorizedException()

            val users = usersRepository.searchByUsername(query)
                .filter { it.id != currentUser.id }

            coroutineScope {
                users.map { user ->
                    async {
                        val friendshipState = try {
                            friendsRepository.getFriendshipActionState(currentUser.id, user.id)
                        } catch (e: Exception) {
                            FriendshipActionState.NOT_FRIENDS
                        }

                        AddFriendUserUi(
                            user = user.toUi(),
                            friendshipState = friendshipState,
                        )
                    }
                }.awaitAll()
            }
        }
    }
}
