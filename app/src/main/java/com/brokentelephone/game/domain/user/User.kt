package com.brokentelephone.game.domain.user

import com.brokentelephone.game.domain.settings.NotificationType

data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val enabledNotifications: List<NotificationType> = NotificationType.entries,
    val authProvider: AuthProvider,
)
