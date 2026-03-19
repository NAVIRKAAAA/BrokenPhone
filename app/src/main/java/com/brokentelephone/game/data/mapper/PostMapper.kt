package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.ext.toMillis
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostStatus
import com.google.firebase.Timestamp

private object PostFields {
    const val ID = "id"
    const val CHAIN_ID = "chainId"
    const val AUTHOR_ID = "authorId"
    const val AUTHOR_NAME = "authorName"
    const val AVATAR_URL = "avatarUrl"
    const val CONTENT = "content"
    const val CREATED_AT = "createdAt"
    const val UPDATED_AT = "updatedAt"
    const val STATUS = "status"
    const val GENERATION = "generation"
    const val MAX_GENERATIONS = "maxGenerations"
    const val TEXT_TIME_LIMIT = "textTimeLimit"
    const val DRAWING_TIME_LIMIT = "drawingTimeLimit"
    const val SESSION_ID = "sessionId"
}

fun Post.toMap(): Map<String, Any?> = mapOf(
    PostFields.ID to id,
    PostFields.CHAIN_ID to chainId,
    PostFields.AUTHOR_ID to authorId,
    PostFields.AUTHOR_NAME to authorName,
    PostFields.AVATAR_URL to avatarUrl,
    PostFields.CONTENT to content.toMap(),
    PostFields.CREATED_AT to createdAt.toTimestamp(),
    PostFields.UPDATED_AT to updatedAt.toTimestamp(),
    PostFields.STATUS to status.name,
    PostFields.SESSION_ID to sessionId,
    PostFields.GENERATION to generation,
    PostFields.MAX_GENERATIONS to maxGenerations,
    PostFields.TEXT_TIME_LIMIT to textTimeLimit,
    PostFields.DRAWING_TIME_LIMIT to drawingTimeLimit,
)

@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.toPost(): Post? {
    return try {
        Post(
            id = this[PostFields.ID] as? String ?: return null,
            chainId = this[PostFields.CHAIN_ID] as? String ?: return null,
            authorId = this[PostFields.AUTHOR_ID] as? String ?: return null,
            authorName = this[PostFields.AUTHOR_NAME] as? String ?: return null,
            avatarUrl = this[PostFields.AVATAR_URL] as? String,
            content = (this[PostFields.CONTENT] as? Map<String, Any?>)?.toPostContent() ?: return null,
            createdAt = (this[PostFields.CREATED_AT] as? Timestamp)?.toMillis() ?: 0L,
            updatedAt = (this[PostFields.UPDATED_AT] as? Timestamp)?.toMillis() ?: 0L,
            status = (this[PostFields.STATUS] as? String)?.let { runCatching { PostStatus.valueOf(it) }.getOrNull() } ?: PostStatus.AVAILABLE,
            sessionId = this[PostFields.SESSION_ID] as? String,
            generation = (this[PostFields.GENERATION] as? Long)?.toInt() ?: return null,
            maxGenerations = (this[PostFields.MAX_GENERATIONS] as? Long)?.toInt() ?: return null,
            textTimeLimit = (this[PostFields.TEXT_TIME_LIMIT] as? Long)?.toInt() ?: return null,
            drawingTimeLimit = (this[PostFields.DRAWING_TIME_LIMIT] as? Long)?.toInt() ?: return null,
        )
    } catch (_: Exception) {
        null
    }
}
