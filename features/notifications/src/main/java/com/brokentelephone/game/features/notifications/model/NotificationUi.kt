package com.brokentelephone.game.features.notifications.model

import androidx.annotation.StringRes
import com.brokentelephone.game.core.R

enum class FriendNotificationType(@param:StringRes val textResId: Int) {
    INVITE_RECEIVED(R.string.notification_friend_request_body),
    INVITE_ACCEPTED(R.string.notification_friend_accepted_body),
}

sealed class NotificationUi {
    abstract val id: String
    abstract val timestamp: Long
    abstract val isRead: Boolean

    data class News(
        override val id: String,
        override val timestamp: Long,
        override val isRead: Boolean,
        val title: String,
        val body: String,
    ) : NotificationUi()

    data class Chain(
        override val id: String,
        override val timestamp: Long,
        override val isRead: Boolean,
        val chainId: String,
        val title: String,
        val body: String,
    ) : NotificationUi()

    data class Friends(
        override val id: String,
        override val timestamp: Long,
        override val isRead: Boolean,
        val userId: String,
        val username: String,
        val userAvatarUrl: String?,
        val type: FriendNotificationType,
    ) : NotificationUi()
}
