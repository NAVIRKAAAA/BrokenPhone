package com.brokentelephone.game.domain.model.permissions

data class UserPermissions(
    val isNotificationsGranted: Boolean = false,
) {
    fun toMap(): Map<String, Any?> = mapOf(
        FIELD_IS_NOTIFICATIONS_GRANTED to isNotificationsGranted,
    )

    companion object {
        const val FIELD_IS_NOTIFICATIONS_GRANTED = "isNotificationsGranted"

        fun fromMap(map: Map<String, Any?>): UserPermissions = UserPermissions(
            isNotificationsGranted = map[FIELD_IS_NOTIFICATIONS_GRANTED] as? Boolean ?: false,
        )
    }
}
