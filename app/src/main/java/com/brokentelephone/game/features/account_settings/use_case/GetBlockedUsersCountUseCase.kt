package com.brokentelephone.game.features.account_settings.use_case

import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBlockedUsersCountUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<Int> = userSession.getBlockedUsers().map { it.size }
}
