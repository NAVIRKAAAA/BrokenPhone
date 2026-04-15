package com.brokentelephone.game.core.model.user

import com.brokentelephone.game.domain.user.User

data class UserUi(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val bio: String = "",
    val createdAt: Long,
    val sessionId: String? = null,
    val friendIds: List<String> = listOf(),
    val blockedUsersIds: List<String> = listOf(),
    val readNotificationIds: List<String> = listOf(),
)

fun User.toUi() = UserUi(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    bio = bio,
    createdAt = createdAt,
    sessionId = sessionId,
    friendIds = friendIds,
    blockedUsersIds = blockedUserIds,
    readNotificationIds = readNotificationIds,
)
