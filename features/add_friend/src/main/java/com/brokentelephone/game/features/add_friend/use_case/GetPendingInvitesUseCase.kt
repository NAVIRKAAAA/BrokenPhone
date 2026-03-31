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
import kotlinx.coroutines.flow.first

class GetPendingInvitesUseCase(
    private val friendsRepository: FriendsRepository,
    private val usersRepository: UsersRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<List<AddFriendUserUi>> {
        return handler.handle(Dispatchers.IO) {
            val currentUser = userSession.authState.first().getUserOrNull()
                ?: throw UnauthorizedException()

            val pendingRequests = friendsRepository.getSentPendingRequests(currentUser.id)
            val receiverIds = pendingRequests.map { it.receiverId }

            if (receiverIds.isEmpty()) return@handle emptyList()

            val users = usersRepository.getUsersByIds(receiverIds)

            users.map { user ->
                AddFriendUserUi(
                    user = user.toUi(),
                    friendshipState = FriendshipActionState.INVITE_SENT,
                )
            }
        }
    }
}
