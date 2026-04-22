package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.NotificationDto
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.model.notification.NotificationData

private enum class NotificationType {
    FRIEND_REQUEST,
    CHAIN_INFO,
    NEWS,
}

fun NotificationDto.toNotification(): Notification? {
    val type = runCatching { NotificationType.valueOf(type) }.getOrNull() ?: return null

    val data: NotificationData = when (type) {
        NotificationType.FRIEND_REQUEST -> NotificationData.Friends(
            requestId = requestId ?: return null,
            userId = senderId ?: return null,
            username = senderUsername ?: return null,
            userAvatarUrl = senderAvatarUrl,
            type = friendsType
                ?.let { runCatching { NotificationData.FriendsType.valueOf(it) }.getOrNull() }
                ?: NotificationData.FriendsType.INVITE_RECEIVED,
        )
        NotificationType.CHAIN_INFO -> NotificationData.ChainInfo(
            chainId = chainId ?: return null,
            postId = postId ?: return null,
            title = title ?: return null,
            body = body ?: return null,
        )
        NotificationType.NEWS -> NotificationData.News(
            title = title ?: return null,
            body = body ?: return null,
        )
    }

    return Notification(
        id = id,
        receiversIds = receiverIds,
        data = data,
        createdAt = createdAt,
    )
}
