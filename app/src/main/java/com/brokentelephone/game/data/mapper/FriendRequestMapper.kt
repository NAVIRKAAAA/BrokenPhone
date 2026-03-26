package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.ext.toMillis
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.model.friend.FriendRequest
import com.brokentelephone.game.domain.model.friend.FriendRequestStatus
import com.google.firebase.Timestamp

private object FriendRequestFields {
    const val ID = "id"
    const val SENDER_ID = "senderId"
    const val RECEIVER_ID = "receiverId"
    const val CREATED_AT = "createdAt"
    const val STATUS = "status"
}

fun FriendRequest.toMap(): Map<String, Any?> = mapOf(
    FriendRequestFields.ID to id,
    FriendRequestFields.SENDER_ID to senderId,
    FriendRequestFields.RECEIVER_ID to receiverId,
    FriendRequestFields.CREATED_AT to createdAt.toTimestamp(),
    FriendRequestFields.STATUS to status.name,
)

fun Map<String, Any?>.toFriendRequest(): FriendRequest? {
    return try {
        FriendRequest(
            id = this[FriendRequestFields.ID] as? String ?: return null,
            senderId = this[FriendRequestFields.SENDER_ID] as? String ?: return null,
            receiverId = this[FriendRequestFields.RECEIVER_ID] as? String ?: return null,
            createdAt = (this[FriendRequestFields.CREATED_AT] as? Timestamp)?.toMillis() ?: return null,
            status = (this[FriendRequestFields.STATUS] as? String)
                ?.let { runCatching { FriendRequestStatus.valueOf(it) }.getOrNull() }
                ?: return null,
        )
    } catch (_: Exception) {
        null
    }
}
