package com.broken.telephone.features.blocked_users.use_case

import com.broken.telephone.domain.repository.UsersRepository
import com.broken.telephone.domain.user.UserSession
import com.broken.telephone.features.blocked_users.model.BlockedUserUi
import com.broken.telephone.features.blocked_users.model.toUi
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
