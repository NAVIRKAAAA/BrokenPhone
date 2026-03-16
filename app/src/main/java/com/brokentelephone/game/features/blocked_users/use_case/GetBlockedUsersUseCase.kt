package com.brokentelephone.game.features.blocked_users.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.features.blocked_users.model.BlockedUserUi
import com.brokentelephone.game.features.blocked_users.model.toUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetBlockedUsersUseCase(
    private val userSession: UserSession,
    private val usersRepository: UsersRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<List<BlockedUserUi>> {
        return handler.handle(Dispatchers.IO) {
            val blockedUsers = userSession.getBlockedUsers()
            coroutineScope {
                blockedUsers.map { blocked ->
                    async {
                        val user = usersRepository.getUserById(blocked.userId) ?: return@async null
                        blocked.toUi(name = user.username, avatarUrl = user.avatarUrl)
                    }
                }.awaitAll().filterNotNull()
            }
        }
    }
}
