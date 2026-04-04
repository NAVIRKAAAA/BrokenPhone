package com.brokentelephone.game.core.model.user

import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.User

data class UserUi(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val bio: String = "",
    val authProvider: AuthProvider = AuthProvider.EMAIL,
    val createdAt: Long,
    val sessionId: String? = null,
    val friendIds: List<String> = listOf(),
    val blockedUsersIds: List<String> = listOf(),
    val readNotificationIds: List<String> = listOf(),
    val isEmailVerified: Boolean = false,
    val language: Language = Language.ENGLISH,
)

fun User.toUi() = UserUi(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    bio = bio,
    authProvider = authProvider,
    createdAt = createdAt,
    sessionId = sessionId,
    friendIds = friendIds,
    blockedUsersIds = blockedUserIds,
    readNotificationIds = readNotificationIds,
    isEmailVerified = isEmailVerified,
    language = language,
)
