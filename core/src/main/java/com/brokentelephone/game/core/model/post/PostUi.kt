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
    val nextTimeLimit: Int,
    val sessionsHistory: List<PostSessionHistoryItem> = emptyList(),
) {
    val isCompleted: Boolean get() = generation == maxGenerations

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
    nextTimeLimit = when (content) {
        is PostContent.Text -> drawingTimeLimit
        is PostContent.Drawing -> textTimeLimit
    },
    sessionsHistory = sessionsHistory,
)
