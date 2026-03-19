package com.brokentelephone.game.domain.model.session

enum class PostSessionHistoryType {
    JOIN,
    CANCEL,
    AUTO_CANCEL,
    COMPLETE,
}

data class PostSessionHistoryItem(
    val sessionId: String,
    val userId: String,
    val type: PostSessionHistoryType,
    val timestamp: Long,
)

private val CANCEL_TYPES = setOf(PostSessionHistoryType.CANCEL, PostSessionHistoryType.AUTO_CANCEL)

fun List<PostSessionHistoryItem>.cooldownRemainingMs(userId: String): Long {
    val now = System.currentTimeMillis()
    val lastCancel = lastOrNull { it.userId == userId && it.type in CANCEL_TYPES } ?: return 0L
    val hasOtherJoinAfter = any {
        it.userId != userId &&
        it.type == PostSessionHistoryType.JOIN &&
        it.timestamp > lastCancel.timestamp
    }
    if (hasOtherJoinAfter) return 0L
    return (SessionConstants.CANCEL_COOLDOWN_MS - (now - lastCancel.timestamp)).coerceAtLeast(0L)
}
