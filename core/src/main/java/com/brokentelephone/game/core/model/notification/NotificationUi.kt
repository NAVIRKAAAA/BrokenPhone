package com.brokentelephone.game.core.model.notification

import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.model.notification.NotificationData

sealed class NotificationUi {
    abstract val id: String
    abstract val createdAt: Long
    abstract val isRead: Boolean

    data class Friends(
        override val id: String,
        override val createdAt: Long,
        override val isRead: Boolean,
        val requestId: String,
        val userId: String,
        val username: String,
        val userAvatarUrl: String?,
        val type: NotificationData.FriendsType,
    ) : NotificationUi()

    data class ChainInfo(
        override val id: String,
        override val createdAt: Long,
        override val isRead: Boolean,
        val chainId: String,
        val postId: String,
        val title: String,
        val body: String,
    ) : NotificationUi()

    data class News(
        override val id: String,
        override val createdAt: Long,
        override val isRead: Boolean,
        val title: String,
        val body: String,
    ) : NotificationUi()
}

fun Notification.toUi(): NotificationUi {
    return when (val d = data) {
        is NotificationData.Friends -> NotificationUi.Friends(
            id = id,
            createdAt = createdAt,
            isRead = false,
            requestId = d.requestId,
            userId = d.userId,
            username = d.username,
            userAvatarUrl = d.userAvatarUrl,
            type = d.type,
        )
        is NotificationData.ChainInfo -> NotificationUi.ChainInfo(
            id = id,
            createdAt = createdAt,
            isRead = false,
            chainId = d.chainId,
            postId = d.postId,
            title = d.title,
            body = d.body,
        )
        is NotificationData.News -> NotificationUi.News(
            id = id,
            createdAt = createdAt,
            isRead = false,
            title = d.title,
            body = d.body,
        )
    }
}
