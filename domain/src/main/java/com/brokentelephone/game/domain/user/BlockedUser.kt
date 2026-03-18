package com.brokentelephone.game.domain.user

data class BlockedUser(
    val id: String,
    val userId: String,
    val createdAt: Long,
) {

    fun toMap(): Map<String, Any?> = mapOf(
        FIELD_ID to id,
        FIELD_USER_ID to userId,
        FIELD_CREATED_AT to createdAt,
    )

    companion object {
        const val FIELD_ID = "id"
        const val FIELD_USER_ID = "userId"
        const val FIELD_CREATED_AT = "createdAt"

        fun fromMap(map: Map<String, Any?>): BlockedUser? {
            return try {
                BlockedUser(
                    id = map[FIELD_ID] as? String ?: return null,
                    userId = map[FIELD_USER_ID] as? String ?: return null,
                    createdAt = map[FIELD_CREATED_AT] as? Long ?: 0L,
                )
            } catch (_: Exception) {
                null
            }
        }
    }
}
