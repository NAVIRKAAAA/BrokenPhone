package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class GetUnreadNotificationsCountUseCase(
    private val repository: NotificationsRepository,
    private val userSession: UserSession,
) {

    // TODO: Need review
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<Int> {
        return userSession.user
            .distinctUntilChangedBy { it?.id }
            .flatMapLatest { user ->
                if (user != null) repository.getUnreadNotificationsCount(user.id, user.readNotificationIds)
                else flowOf(0)
            }
    }
}
