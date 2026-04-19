package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.PostDto
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus

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
    const val SESSIONS_HISTORY = "sessionsHistory"
}

fun PostDto.toPost(): Post = Post(
    id = id,
    chainId = chainId,
    authorId = authorId,
    authorName = author?.username ?: authorName,
    avatarUrl = author?.avatarUrl ?: avatarUrl,
    content = when (contentType) {
        "DRAWING" -> PostContent.Drawing(imageUrl = contentImageUrl)
        else -> PostContent.Text(text = contentText.orEmpty())
    },
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = PostStatus.fromString(status),
    sessionId = sessionId,
    sessionsHistory = sessionsHistory.mapNotNull { it.toPostSessionHistoryItem() },
    generation = generation,
    maxGenerations = maxGenerations,
    textTimeLimit = textTimeLimit,
    drawingTimeLimit = drawingTimeLimit,
)

fun Post.toPostDto(): PostDto = PostDto(
    id = id,
    chainId = chainId,
    authorId = authorId,
    authorName = authorName,
    avatarUrl = avatarUrl,
    contentType = when (content) {
        is PostContent.Text -> "TEXT"
        is PostContent.Drawing -> "DRAWING"
    },
    contentText = (content as? PostContent.Text)?.text,
    contentImageUrl = (content as? PostContent.Drawing)?.imageUrl,
    status = status.name,
    sessionId = sessionId,
    sessionsHistory = sessionsHistory.map { it.toDto() },
    generation = generation,
    maxGenerations = maxGenerations,
    textTimeLimit = textTimeLimit,
    drawingTimeLimit = drawingTimeLimit,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

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
    PostFields.SESSIONS_HISTORY to sessionsHistory.map { it.toMap() },
    PostFields.GENERATION to generation,
    PostFields.MAX_GENERATIONS to maxGenerations,
    PostFields.TEXT_TIME_LIMIT to textTimeLimit,
    PostFields.DRAWING_TIME_LIMIT to drawingTimeLimit,
)