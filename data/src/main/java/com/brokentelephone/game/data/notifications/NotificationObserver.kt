package com.brokentelephone.game.data.notifications

import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

class NotificationObserver(
    private val userSession: UserSession,
    private val notificationsRepository: NotificationsRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observe(): Flow<Notification> = userSession.user
        .distinctUntilChangedBy { it?.id }
        .flatMapLatest { user ->
            if (user != null) notificationsRepository.observeNewNotifications()
            else emptyFlow()
        }
}
