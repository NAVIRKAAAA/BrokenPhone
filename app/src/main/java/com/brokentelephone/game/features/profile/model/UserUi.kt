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
)

fun User.toUi() = UserUi(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    authProvider = authProvider,
    createdAt = createdAt,
)
