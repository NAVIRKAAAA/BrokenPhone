package com.brokentelephone.game.features.dashboard.model

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