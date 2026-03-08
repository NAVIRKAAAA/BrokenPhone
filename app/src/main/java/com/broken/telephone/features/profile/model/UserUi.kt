package com.broken.telephone.features.profile.model

import com.broken.telephone.domain.user.AuthProvider
import com.broken.telephone.domain.user.User

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
