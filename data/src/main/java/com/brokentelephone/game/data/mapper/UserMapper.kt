package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.UserDto
import com.brokentelephone.game.data.dto.UserPermissionsDto
import com.brokentelephone.game.domain.model.permissions.UserPermissions
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User
import io.github.jan.supabase.auth.user.UserInfo

fun UserDto.toUser(): User = User(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    bio = bio,
    notifications = notifications.mapNotNull { NotificationType.getByName(it) }.ifEmpty { NotificationType.entries },
    onboardingStep = OnboardingStep.getByName(onboardingStep),
    createdAt = createdAt,
    updatedAt = updatedAt,
    readNotificationIds = readNotificationIds,
    fcmToken = fcmToken,
    sessionId = sessionId,
    permissions = UserPermissions(isNotificationsGranted = permissions.isNotificationsGranted),
)

fun UserInfo.toUser(): User {
    val now = System.currentTimeMillis()
    return User(
        id = id,
        username = "",
        email = email ?: "",
        avatarUrl = null,
        bio = "",
        notifications = NotificationType.entries,
        onboardingStep = OnboardingStep.CHOOSE_AVATAR,
        createdAt = createdAt?.toEpochMilliseconds() ?: now,
        updatedAt = updatedAt?.toEpochMilliseconds() ?: now,
    )
}

fun User.toUserDto(): UserDto = UserDto(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    bio = bio,
    notifications = notifications.map { it.name },
    onboardingStep = onboardingStep.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
    readNotificationIds = readNotificationIds,
    fcmToken = fcmToken,
    sessionId = sessionId,
    permissions = UserPermissionsDto(isNotificationsGranted = permissions.isNotificationsGranted),
)
