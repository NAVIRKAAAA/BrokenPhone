package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class GetUnreadNotificationsCountUseCase(
    private val repository: NotificationsRepository,
    private val userSession: UserSession,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<Int> {
        return userSession.getUser()
            .flatMapLatest { user ->
                if (user == null) flowOf(0)
                else repository.getUnreadNotificationsCount(user.id, user.readNotificationIds)
            }
    }
}
