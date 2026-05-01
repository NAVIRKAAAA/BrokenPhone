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
    val isEmailVerified: Boolean = false,
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
    readNotificationIds = readNotificationIds,
    isEmailVerified = isEmailVerified
)
