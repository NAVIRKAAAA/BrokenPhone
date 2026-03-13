package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.ext.toMillis
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.post.Post
import com.google.firebase.Timestamp

private object PostFields {
    const val ID = "id"
    const val AUTHOR_ID = "authorId"
    const val AUTHOR_NAME = "authorName"
    const val AVATAR_URL = "avatarUrl"
    const val CREATED_AT = "createdAt"
    const val UPDATED_AT = "updatedAt"
    const val GENERATION = "generation"
    const val MAX_GENERATIONS = "maxGenerations"
    const val TEXT_TIME_LIMIT = "textTimeLimit"
    const val DRAWING_TIME_LIMIT = "drawingTimeLimit"
    const val CURRENT_ENTRY = "currentEntry"
}

fun Post.toMap(): Map<String, Any?> = mapOf(
    PostFields.ID to id,
    PostFields.AUTHOR_ID to authorId,
    PostFields.AUTHOR_NAME to authorName,
    PostFields.AVATAR_URL to avatarUrl,
    PostFields.CREATED_AT to createdAt.toTimestamp(),
    PostFields.UPDATED_AT to updatedAt.toTimestamp(),
    PostFields.GENERATION to generation,
    PostFields.MAX_GENERATIONS to maxGenerations,
    PostFields.TEXT_TIME_LIMIT to textTimeLimit,
    PostFields.DRAWING_TIME_LIMIT to drawingTimeLimit,
    PostFields.CURRENT_ENTRY to currentEntry.toMap(),
)

@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.toPost(): Post? {
    return try {
        Post(
            id = this[PostFields.ID] as? String ?: return null,
            authorId = this[PostFields.AUTHOR_ID] as? String ?: return null,
            authorName = this[PostFields.AUTHOR_NAME] as? String ?: return null,
            avatarUrl = this[PostFields.AVATAR_URL] as? String,
            createdAt = (this[PostFields.CREATED_AT] as? Timestamp)?.toMillis() ?: 0L,
            updatedAt = (this[PostFields.UPDATED_AT] as? Timestamp)?.toMillis() ?: 0L,
            generation = (this[PostFields.GENERATION] as? Long)?.toInt() ?: return null,
            maxGenerations = (this[PostFields.MAX_GENERATIONS] as? Long)?.toInt() ?: return null,
            textTimeLimit = (this[PostFields.TEXT_TIME_LIMIT] as? Long)?.toInt() ?: return null,
            drawingTimeLimit = (this[PostFields.DRAWING_TIME_LIMIT] as? Long)?.toInt() ?: return null,
            currentEntry = (this[PostFields.CURRENT_ENTRY] as? Map<String, Any?>)?.toPostChainEntry() ?: return null,
        )
    } catch (_: Exception) {
        null
    }
}
