package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.ext.toMillis
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.model.post.PostChainEntry
import com.brokentelephone.game.domain.model.post.PostStatus
import com.google.firebase.Timestamp

private object PostChainEntryFields {
    const val ID = "id"
    const val PARENT_ID = "parentId"
    const val AUTHOR_ID = "authorId"
    const val AUTHOR_NAME = "authorName"
    const val AVATAR_URL = "avatarUrl"
    const val CONTENT = "content"
    const val CREATED_AT = "createdAt"
    const val UPDATED_AT = "updatedAt"
    const val STATUS = "status"
}

fun PostChainEntry.toMap(): Map<String, Any?> = mapOf(
    PostChainEntryFields.ID to id,
    PostChainEntryFields.PARENT_ID to parentId,
    PostChainEntryFields.AUTHOR_ID to authorId,
    PostChainEntryFields.AUTHOR_NAME to authorName,
    PostChainEntryFields.AVATAR_URL to avatarUrl,
    PostChainEntryFields.CONTENT to content.toMap(),
    PostChainEntryFields.CREATED_AT to createdAt.toTimestamp(),
    PostChainEntryFields.UPDATED_AT to updatedAt.toTimestamp(),
    PostChainEntryFields.STATUS to status.name,
)

@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.toPostChainEntry(): PostChainEntry? {
    return try {
        PostChainEntry(
            id = this[PostChainEntryFields.ID] as? String ?: return null,
            parentId = this[PostChainEntryFields.PARENT_ID] as? String ?: return null,
            authorId = this[PostChainEntryFields.AUTHOR_ID] as? String ?: return null,
            authorName = this[PostChainEntryFields.AUTHOR_NAME] as? String ?: return null,
            avatarUrl = this[PostChainEntryFields.AVATAR_URL] as? String,
            content = (this[PostChainEntryFields.CONTENT] as? Map<String, Any?>)?.toPostContent() ?: return null,
            createdAt = (this[PostChainEntryFields.CREATED_AT] as? Timestamp)?.toMillis() ?: 0L,
            updatedAt = (this[PostChainEntryFields.UPDATED_AT] as? Timestamp)?.toMillis() ?: 0L,
            status = PostStatus.fromString(this[PostChainEntryFields.STATUS] as? String),
        )
    } catch (_: Exception) {
        null
    }
}
