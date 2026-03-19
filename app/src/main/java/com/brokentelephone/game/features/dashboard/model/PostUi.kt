package com.brokentelephone.game.features.dashboard.model

import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus

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
) {
    val isCompleted: Boolean get() = generation == maxGenerations
}