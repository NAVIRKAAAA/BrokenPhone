package com.brokentelephone.game.domain.user

import com.brokentelephone.game.domain.model.settings.NotificationType

data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val notifications: List<NotificationType>,
    val authProvider: AuthProvider,
    val onboardingStep: OnboardingStep,
    val blockedUserIds: List<String> = emptyList(),
    val blockedBy: List<String> = emptyList(),
    val notInterestedPostIds: List<String> = emptyList(),
    val sessionId: String? = null,
    val isEmailVerified: Boolean = false,
) {

    fun toMap(): Map<String, Any?> = mapOf(
        FIELD_ID to id,
        FIELD_USERNAME to username,
        FIELD_EMAIL to email,
        FIELD_AVATAR_URL to avatarUrl,
        FIELD_AUTH_PROVIDER to authProvider.name,
        FIELD_NOTIFICATIONS to notifications.map { it.name },
        FIELD_ONBOARDING_STEP to onboardingStep.name,
        FIELD_CREATED_AT to createdAt,
        FIELD_UPDATED_AT to updatedAt,
        FIELD_BLOCKED_USER_IDS to blockedUserIds,
        FIELD_BLOCKED_BY to blockedBy,
        FIELD_NOT_INTERESTED_POST_IDS to notInterestedPostIds,
        FIELD_SESSION_ID to sessionId,
        FIELD_IS_EMAIL_VERIFIED to isEmailVerified,
    )

    companion object {
        const val FIELD_ID = "id"
        const val FIELD_USERNAME = "username"
        const val FIELD_EMAIL = "email"
        const val FIELD_AVATAR_URL = "avatarUrl"
        const val FIELD_AUTH_PROVIDER = "authProvider"
        const val FIELD_NOTIFICATIONS = "notifications"
        const val FIELD_ONBOARDING_STEP = "onboardingStep"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
        const val FIELD_BLOCKED_USER_IDS = "blockedUserIds"
        const val FIELD_BLOCKED_BY = "blockedBy"
        const val FIELD_NOT_INTERESTED_POST_IDS = "notInterestedPostIds"
        const val FIELD_SESSION_ID = "sessionId"
        const val FIELD_IS_EMAIL_VERIFIED = "isEmailVerified"

        @Suppress("UNCHECKED_CAST")
        fun fromMap(map: Map<String, Any?>): User? {
            return try {
                User(
                    id = map[FIELD_ID] as? String ?: return null,
                    username = map[FIELD_USERNAME] as? String ?: return null,
                    email = map[FIELD_EMAIL] as? String ?: "",
                    avatarUrl = map[FIELD_AVATAR_URL] as? String,
                    authProvider = AuthProvider.valueOf(map[FIELD_AUTH_PROVIDER] as? String ?: return null),
                    notifications = (map[FIELD_NOTIFICATIONS] as? List<String>)
                        ?.mapNotNull { runCatching { NotificationType.valueOf(it) }.getOrNull() }
                        ?: NotificationType.entries,
                    onboardingStep = runCatching {
                        OnboardingStep.valueOf(map[FIELD_ONBOARDING_STEP] as? String ?: "")
                    }.getOrDefault(OnboardingStep.CHOOSE_USERNAME),
                    createdAt = map[FIELD_CREATED_AT] as? Long ?: 0L,
                    updatedAt = map[FIELD_UPDATED_AT] as? Long ?: 0L,
                    blockedUserIds = (map[FIELD_BLOCKED_USER_IDS] as? List<String>) ?: emptyList(),
                    blockedBy = (map[FIELD_BLOCKED_BY] as? List<String>) ?: emptyList(),
                    notInterestedPostIds = (map[FIELD_NOT_INTERESTED_POST_IDS] as? List<String>) ?: emptyList(),
                    sessionId = map[FIELD_SESSION_ID] as? String,
                    isEmailVerified = map[FIELD_IS_EMAIL_VERIFIED] as? Boolean ?: false,
                )
            } catch (_: Exception) {
                null
            }
        }
    }
}
