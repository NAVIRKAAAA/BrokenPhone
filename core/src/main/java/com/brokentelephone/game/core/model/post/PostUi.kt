package com.brokentelephone.game.core.model.post

import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.domain.model.session.PostSessionHistoryItem


data class PostUi(
    val id: String,
    val authorId: String,
    val authorName: String,
    val avatarUrl: String?,
    val content: PostContent,
    val createdAt: Long,
    val generation: Int,
    val maxGenerations: Int,
    val status: PostStatus,
    val drawingTimeLimit: Int,
    val textTimeLimit: Int,
    val sessionsHistory: List<PostSessionHistoryItem> = emptyList(),
    val chainSize: Int? = 0
) {

    val isCompleted: Boolean get() = generation == maxGenerations

    val nextTimeLimit: Int
        get() = when (content) {
            is PostContent.Text -> drawingTimeLimit
            is PostContent.Drawing -> textTimeLimit
        }

    val timeLimit: Int
        get() = when (content) {
            is PostContent.Text -> textTimeLimit
            is PostContent.Drawing -> drawingTimeLimit
        }
}

fun Post.toUi() = PostUi(
    id = id,
    authorId = authorId,
    authorName = authorName,
    avatarUrl = avatarUrl,
    content = content,
    createdAt = updatedAt,
    generation = generation,
    maxGenerations = maxGenerations,
    status = status,
    drawingTimeLimit = drawingTimeLimit,
    textTimeLimit = textTimeLimit,
    sessionsHistory = sessionsHistory,
    chainSize = chainSize
)
