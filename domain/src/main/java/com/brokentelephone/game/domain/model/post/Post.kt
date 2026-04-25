package com.brokentelephone.game.domain.model.post

import com.brokentelephone.game.domain.model.session.PostSessionHistoryItem

data class Post(
    val id: String,
    val chainId: String,
    val authorId: String,
    val authorName: String,
    val avatarUrl: String?,
    val content: PostContent,
    val createdAt: Long,
    val updatedAt: Long,
    val status: PostStatus,
    val sessionsHistory: List<PostSessionHistoryItem> = emptyList(),

    // Chain info
    val generation: Int,
    val maxGenerations: Int,

    // Time limits
    val textTimeLimit: Int,
    val drawingTimeLimit: Int,
)
