package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUnreadNotificationsCountUseCase(
    private val userSession: UserSession,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute() : Flow<Int> {
        return userSession.unreadNotifications.map { it.size }
    }
}
