package com.broken.telephone.features.dashboard.model

import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus

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
) {
    val isCompleted: Boolean get() = generation == maxGenerations
}