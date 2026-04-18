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
    val friendIds: List<String> = emptyList(),
    val notInterestedPostIds: List<String> = emptyList(),
    val readNotificationIds: List<String> = emptyList(),
    val fcmToken: String? = null,
    val sessionId: String? = null,
    val permissions: UserPermissions = UserPermissions(),
) {

    companion object {
        const val FIELD_ID = "id"
        const val FIELD_USERNAME = "username"
        const val FIELD_EMAIL = "email"
        const val FIELD_AVATAR_URL = "avatarUrl"
        const val FIELD_BIO = "bio"
        const val FIELD_NOTIFICATIONS = "notifications"
        const val FIELD_ONBOARDING_STEP = "onboardingStep"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
        const val FIELD_FRIEND_IDS = "friendIds"
        const val FIELD_NOT_INTERESTED_POST_IDS = "notInterestedPostIds"
        const val FIELD_READ_NOTIFICATION_IDS = "readNotificationIds"
        const val FIELD_FCM_TOKEN = "fcmToken"
        const val FIELD_SESSION_ID = "sessionId"
        const val FIELD_PERMISSIONS = "permissions"

        @Suppress("UNCHECKED_CAST")
        fun fromMap(map: Map<String, Any?>): User? {
            return try {
                User(
                    id = map[FIELD_ID] as? String ?: return null,
                    username = map[FIELD_USERNAME] as? String ?: return null,
                    email = map[FIELD_EMAIL] as? String ?: "",
                    avatarUrl = map[FIELD_AVATAR_URL] as? String,
                    bio = map[FIELD_BIO] as? String ?: "",
                    notifications = (map[FIELD_NOTIFICATIONS] as? List<String>)
                        ?.mapNotNull { runCatching { NotificationType.valueOf(it) }.getOrNull() }
                        ?: NotificationType.entries,
                    onboardingStep = runCatching {
                        OnboardingStep.valueOf(map[FIELD_ONBOARDING_STEP] as? String ?: "")
                    }.getOrDefault(OnboardingStep.CHOOSE_USERNAME),
                    createdAt = map[FIELD_CREATED_AT] as? Long ?: 0L,
                    updatedAt = map[FIELD_UPDATED_AT] as? Long ?: 0L,
                    friendIds = (map[FIELD_FRIEND_IDS] as? List<String>) ?: emptyList(),
                    notInterestedPostIds = (map[FIELD_NOT_INTERESTED_POST_IDS] as? List<String>) ?: emptyList(),
                    readNotificationIds = (map[FIELD_READ_NOTIFICATION_IDS] as? List<String>) ?: emptyList(),
                    fcmToken = map[FIELD_FCM_TOKEN] as? String,
                    sessionId = map[FIELD_SESSION_ID] as? String,
                    permissions = (map[FIELD_PERMISSIONS] as? Map<String, Any?>)
                        ?.let { UserPermissions.fromMap(it) }
                        ?: UserPermissions(),
                )
            } catch (_: Exception) {
                null
            }
        }
    }
}
