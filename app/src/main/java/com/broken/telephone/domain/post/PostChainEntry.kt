package com.broken.telephone.domain.post

data class PostChainEntry(
    val authorId: String,
    val authorName: String,
    val avatarUrl: String?,
    val content: PostContent,
    val createdAt: Long,
    val status: PostStatus,
    val lockedBy: String?,
)
