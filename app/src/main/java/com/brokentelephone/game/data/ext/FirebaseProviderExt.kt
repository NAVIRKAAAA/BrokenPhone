package com.brokentelephone.game.data.ext

import com.brokentelephone.game.domain.user.AuthProvider

fun String.toAuthProvider(): AuthProvider? = when (this) {
    "google.com" -> AuthProvider.GOOGLE
    "password" -> AuthProvider.EMAIL
    else -> null
}
