package com.brokentelephone.game.domain.model.chain

data class Chain(
    val id: String,
    val createdAt: Long,
    val status: ChainStatus,
    val generation: Int,
    val maxGenerations: Int,
    val textTimeLimit: Int,
    val drawingTimeLimit: Int,
    val completedAt: Long? = null,
)

