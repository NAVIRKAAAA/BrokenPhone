package com.brokentelephone.game.domain.post

data class Post(
    val id: String,
    val parentId: String?,
    val authorId: String,
    val authorName: String,
    val avatarUrl: String?,
    val content: PostContent,
    val createdAt: Long,
    val updatedAt: Long,
    val status: PostStatus,

    // Chain info
    val generation: Int,
    val maxGenerations: Int,

    // Time limits
    val textTimeLimit: Int,
    val drawingTimeLimit: Int,
)
