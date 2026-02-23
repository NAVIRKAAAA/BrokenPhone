package com.broken.telephone.domain.user

import com.broken.telephone.domain.settings.NotificationType

data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val enabledNotifications: List<NotificationType> = NotificationType.entries,
)
