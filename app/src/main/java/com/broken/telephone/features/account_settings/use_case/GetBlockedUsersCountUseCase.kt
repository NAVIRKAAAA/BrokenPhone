package com.broken.telephone.features.account_settings.use_case

import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBlockedUsersCountUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<Int> = userSession.getBlockedUsers().map { it.size }
}
