package com.broken.telephone.features.profile.model

import com.broken.telephone.domain.user.User

data class UserUi(
    val username: String,
    val email: String,
    val avatarUrl: String?,
)

fun User.toUi() = UserUi(
    username = username,
    email = email,
    avatarUrl = avatarUrl,
)
