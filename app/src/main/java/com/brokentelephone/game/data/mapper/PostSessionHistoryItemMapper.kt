package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.ext.toMillis
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.model.session.PostSessionHistoryItem
import com.brokentelephone.game.domain.model.session.PostSessionHistoryType
import com.google.firebase.Timestamp

fun PostSessionHistoryItem.toMap(): Map<String, Any?> = mapOf(
    "sessionId" to sessionId,
    "userId" to userId,
    "type" to type.name,
    "timestamp" to timestamp.toTimestamp(),
)

@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.toPostSessionHistoryItem(): PostSessionHistoryItem? {
    return try {
        PostSessionHistoryItem(
            sessionId = this["sessionId"] as? String ?: return null,
            userId = this["userId"] as? String ?: return null,
            type = (this["type"] as? String)
                ?.let { runCatching { PostSessionHistoryType.valueOf(it) }.getOrNull() }
                ?: return null,
            timestamp = (this["timestamp"] as? Timestamp)?.toMillis()
                ?: (this["timestamp"] as? Long)
                ?: return null,
        )
    } catch (_: Exception) {
        null
    }
}
