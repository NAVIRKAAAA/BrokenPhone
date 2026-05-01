package com.brokentelephone.game.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("username") val username: String,
    @SerialName("email") val email: String = "",
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("bio") val bio: String = "",
    @SerialName("notifications") val notifications: List<String> = emptyList(),
    @SerialName("onboarding_step") val onboardingStep: String,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
    @SerialName("read_notification_ids") val readNotificationIds: List<String> = emptyList(),
    @SerialName("fcm_token") val fcmToken: String? = null,
    @SerialName("session_id") val sessionId: String? = null,
    @SerialName("permissions") val permissions: UserPermissionsDto = UserPermissionsDto(),
)

@Serializable
data class UserPermissionsDto(
    @SerialName("isNotificationsGranted") val isNotificationsGranted: Boolean = false,
)
