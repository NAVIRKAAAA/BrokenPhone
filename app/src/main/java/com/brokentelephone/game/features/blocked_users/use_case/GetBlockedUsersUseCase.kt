package com.brokentelephone.game.features.blocked_users.use_case

import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.features.blocked_users.model.BlockedUserUi
import com.brokentelephone.game.features.blocked_users.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBlockedUsersUseCase(
    private val userSession: UserSession,
    private val usersRepository: UsersRepository,
) {
    operator fun invoke(): Flow<List<BlockedUserUi>> = userSession.getBlockedUsers()
        .map { blockedUsers ->
            val ids = blockedUsers.map { it.userId }
            val users = usersRepository.getUsersById(ids)
            blockedUsers.map { blockedUser ->
                val user = users.find { it.id == blockedUser.userId }
                blockedUser.toUi(
                    name = user?.username.orEmpty(),
                    avatarUrl = user?.avatarUrl,
                )
            }
        }
}
