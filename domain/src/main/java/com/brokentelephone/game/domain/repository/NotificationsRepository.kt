package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.model.notification.NotificationFilter
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {

    suspend fun getNotificationById(notificationId: String): Notification?

    suspend fun getNotifications(userId: String): List<Notification>

    suspend fun getNotifications(userId: String, filter: NotificationFilter): List<Notification>

    fun getUnreadNotificationsCount(userId: String, readNotificationIds: List<String>): Flow<Int>
}
