package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.UserDto
import com.brokentelephone.game.data.dto.UserPermissionsDto
import com.brokentelephone.game.domain.model.permissions.UserPermissions
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User

fun UserDto.toUser(): User = User(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    bio = bio,
    authProvider = AuthProvider.getByName(authProvider),
    notifications = notifications.mapNotNull { NotificationType.getByName(it) }.ifEmpty { NotificationType.entries },
    onboardingStep = OnboardingStep.getByName(onboardingStep),
    createdAt = createdAt,
    updatedAt = updatedAt,
    friendIds = friendIds,
    blockedUserIds = blockedUserIds,
    blockedBy = blockedBy,
    notInterestedPostIds = notInterestedPostIds,
    readNotificationIds = readNotificationIds,
    fcmToken = fcmToken,
    sessionId = sessionId,
    isEmailVerified = isEmailVerified,
    permissions = UserPermissions(isNotificationsGranted = permissions.isNotificationsGranted),
)

fun User.toUserDto(): UserDto = UserDto(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    bio = bio,
    authProvider = authProvider.name,
    notifications = notifications.map { it.name },
    onboardingStep = onboardingStep.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
    friendIds = friendIds,
    blockedUserIds = blockedUserIds,
    blockedBy = blockedBy,
    notInterestedPostIds = notInterestedPostIds,
    readNotificationIds = readNotificationIds,
    fcmToken = fcmToken,
    sessionId = sessionId,
    isEmailVerified = isEmailVerified,
    permissions = UserPermissionsDto(isNotificationsGranted = permissions.isNotificationsGranted),
)
