package com.brokentelephone.game.features.blocked_users.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import com.brokentelephone.game.features.blocked_users.model.BlockedUserUi
import com.brokentelephone.game.features.blocked_users.model.toBlockedUserUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class GetBlockedUsersUseCase(
    private val userSession: UserSession,
    private val usersRepository: UsersRepository,
    private val handler: ApiHandler
) {
    suspend fun execute(): AppResult<List<BlockedUserUi>> {
        return handler.handle(Dispatchers.IO) {
            val user = userSession.authState.firstOrNull()?.getUserOrNull()
                ?: throw UnauthorizedException()

            val blockedUsersIds = user.blockedUserIds

            blockedUsersIds.mapNotNull { userId ->
                usersRepository.getUserById(userId)?.toBlockedUserUi()
            }
        }
    }
}
