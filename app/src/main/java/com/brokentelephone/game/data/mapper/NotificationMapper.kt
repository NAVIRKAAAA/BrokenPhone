package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.ext.toMillis
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.model.notification.NotificationData
import com.google.firebase.Timestamp

private object NotificationFields {
    const val ID = "id"
    const val RECEIVERS_IDS = "receiversIds"
    const val TYPE = "type"
    const val CREATED_AT = "createdAt"

    // Friends
    const val REQUEST_ID = "requestId"
    const val USER_ID = "userId"
    const val USERNAME = "username"
    const val USER_AVATAR_URL = "userAvatarUrl"
    const val FRIENDS_TYPE = "friendsType"

    // ChainCompleted
    const val CHAIN_ID = "chainId"
    const val POST_ID = "postId"
    const val TITLE = "title"
    const val BODY = "body"
}

private enum class NotificationType {
    FRIEND_REQUEST,
    CHAIN_INFO,
    NEWS,
}

fun Notification.toMap(): Map<String, Any?> {
    val dataMap: Map<String, Any?> = when (val d = data) {
        is NotificationData.Friends -> mapOf(
            NotificationFields.TYPE to NotificationType.FRIEND_REQUEST.name,
            NotificationFields.REQUEST_ID to d.requestId,
            NotificationFields.USER_ID to d.userId,
            NotificationFields.USERNAME to d.username,
            NotificationFields.USER_AVATAR_URL to d.userAvatarUrl,
            NotificationFields.FRIENDS_TYPE to d.type.name,
        )
        is NotificationData.ChainInfo -> mapOf(
            NotificationFields.TYPE to NotificationType.CHAIN_INFO.name,
            NotificationFields.CHAIN_ID to d.chainId,
            NotificationFields.POST_ID to d.postId,
            NotificationFields.TITLE to d.title,
            NotificationFields.BODY to d.body,
        )
        is NotificationData.News -> mapOf(
            NotificationFields.TYPE to NotificationType.NEWS.name,
            NotificationFields.TITLE to d.title,
            NotificationFields.BODY to d.body,
        )
    }
    return mapOf(
        NotificationFields.ID to id,
        NotificationFields.RECEIVERS_IDS to receiversIds,
        NotificationFields.CREATED_AT to createdAt.toTimestamp(),
    ) + dataMap
}

@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.toNotification(): Notification? {
    return try {
        val id = this[NotificationFields.ID] as? String ?: return null
        val receiversIds = this[NotificationFields.RECEIVERS_IDS] as? List<String> ?: return null
        val createdAt = (this[NotificationFields.CREATED_AT] as? Timestamp)?.toMillis() ?: return null
        val type = (this[NotificationFields.TYPE] as? String)
            ?.let { runCatching { NotificationType.valueOf(it) }.getOrNull() }
            ?: return null

        val data: NotificationData = when (type) {
            NotificationType.FRIEND_REQUEST -> NotificationData.Friends(
                requestId = this[NotificationFields.REQUEST_ID] as? String ?: return null,
                userId = this[NotificationFields.USER_ID] as? String ?: return null,
                username = this[NotificationFields.USERNAME] as? String ?: return null,
                userAvatarUrl = this[NotificationFields.USER_AVATAR_URL] as? String,
                type = (this[NotificationFields.FRIENDS_TYPE] as? String)
                    ?.let { runCatching { NotificationData.FriendsType.valueOf(it) }.getOrNull() }
                    ?: NotificationData.FriendsType.INVITE_RECEIVED,
            )
            NotificationType.CHAIN_INFO -> NotificationData.ChainInfo(
                chainId = this[NotificationFields.CHAIN_ID] as? String ?: return null,
                postId = this[NotificationFields.POST_ID] as? String ?: return null,
                title = this[NotificationFields.TITLE] as? String ?: return null,
                body = this[NotificationFields.BODY] as? String ?: return null,
            )
            NotificationType.NEWS -> NotificationData.News(
                title = this[NotificationFields.TITLE] as? String ?: return null,
                body = this[NotificationFields.BODY] as? String ?: return null,
            )
        }

        Notification(
            id = id,
            receiversIds = receiversIds,
            data = data,
            createdAt = createdAt,
        )
    } catch (_: Exception) {
        null
    }
}
