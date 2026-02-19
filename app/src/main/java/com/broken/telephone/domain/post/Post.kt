package com.broken.telephone.domain.post

data class Post(
    val id: String,
    val authorId: String,
    val authorName: String,
    val avatarUrl: String?,
    val createdAt: Long,

    // Chain info
    val generation: Int,
    val maxGenerations: Int,

    // Time limits
    val textTimeLimit: Int,
    val drawingTimeLimit: Int,

    // Current chain entry
    val currentEntry: PostChainEntry,
)
