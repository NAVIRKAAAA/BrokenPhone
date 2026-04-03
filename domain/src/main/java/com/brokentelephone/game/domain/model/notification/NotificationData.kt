package com.brokentelephone.game.domain.model.notification

sealed class NotificationData {

    enum class FriendsType {
        INVITE_RECEIVED,
        INVITE_ACCEPTED,
    }

    data class Friends(
        val requestId: String,
        val userId: String,
        val username: String,
        val userAvatarUrl: String?,
        val type: FriendsType,
    ) : NotificationData()

    data class ChainInfo(
        val chainId: String,
        val postId: String,
        val title: String,
        val body: String,
    ) : NotificationData()

    data class News(
        val title: String,
        val body: String,
    ) : NotificationData()

}
