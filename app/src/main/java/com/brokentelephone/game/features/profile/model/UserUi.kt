package com.brokentelephone.game.features.profile.model

import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.User

data class UserUi(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val authProvider: AuthProvider = AuthProvider.EMAIL,
    val createdAt: Long,
    val sessionId: String? = null,
    val blockedUsersIds: List<String> = listOf()
)

fun User.toUi() = UserUi(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    authProvider = authProvider,
    createdAt = createdAt,
    sessionId = sessionId,
    blockedUsersIds = blockedUserIds
)
