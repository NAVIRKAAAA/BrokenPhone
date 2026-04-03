package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.notification.Notification

interface NotificationsRepository {

    suspend fun getNotifications(userId: String): List<Notification>
}
