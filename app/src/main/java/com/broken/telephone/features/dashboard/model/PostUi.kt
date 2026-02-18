package com.broken.telephone.features.dashboard.model

import com.broken.telephone.domain.post.PostContent

data class PostUi(
    val id: String,
    val authorName: String,
    val avatarUrl: String?,
    val content: PostContent,
    val createdAt: Long,
    val generation: Int,
    val maxGenerations: Int,
    val textTimeLimit: Int,
    val drawingTimeLimit: Int,
)
