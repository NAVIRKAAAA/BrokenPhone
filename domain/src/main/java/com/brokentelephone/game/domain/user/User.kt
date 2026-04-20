package com.brokentelephone.game.domain.user

import com.brokentelephone.game.domain.model.permissions.UserPermissions
import com.brokentelephone.game.domain.model.settings.NotificationType

data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val bio: String = "",
    val createdAt: Long,
    val updatedAt: Long,
    val notifications: List<NotificationType>,
    val onboardingStep: OnboardingStep,
    val isEmailVerified: Boolean = false,
    val notInterestedPostIds: List<String> = emptyList(),
    val readNotificationIds: List<String> = emptyList(),
    val fcmToken: String? = null,
    val sessionId: String? = null,
    val permissions: UserPermissions = UserPermissions(),
)
