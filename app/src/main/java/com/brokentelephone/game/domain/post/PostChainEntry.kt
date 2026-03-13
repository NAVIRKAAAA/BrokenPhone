package com.brokentelephone.game.domain.post

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class PostChainEntry(
    val id: String = Uuid.random().toString(),
    val parentId: String,
    val authorId: String,
    val authorName: String,
    val avatarUrl: String?,
    val content: PostContent,
    val createdAt: Long,
    val updatedAt: Long,
    val status: PostStatus,
)
