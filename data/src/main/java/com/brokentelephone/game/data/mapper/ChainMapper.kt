package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.ChainDto
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.model.chain.Chain
import com.brokentelephone.game.domain.model.chain.ChainStatus

private object ChainFields {
    const val ID = "id"
    const val CREATED_AT = "createdAt"
    const val STATUS = "status"
    const val GENERATION = "generation"
    const val MAX_GENERATIONS = "maxGenerations"
    const val TEXT_TIME_LIMIT = "textTimeLimit"
    const val DRAWING_TIME_LIMIT = "drawingTimeLimit"
    const val COMPLETED_AT = "completedAt"
}

fun ChainDto.toChain(): Chain = Chain(
    id = id,
    createdAt = createdAt,
    status = runCatching { ChainStatus.valueOf(status) }.getOrDefault(ChainStatus.ACTIVE),
    generation = 0,
    maxGenerations = maxGenerations,
    textTimeLimit = textTimeLimit,
    drawingTimeLimit = drawingTimeLimit,
    completedAt = completedAt,
)

fun Chain.toChainDto(): ChainDto = ChainDto(
    id = id,
    status = status.name,
    maxGenerations = maxGenerations,
    textTimeLimit = textTimeLimit,
    drawingTimeLimit = drawingTimeLimit,
    createdAt = createdAt,
    completedAt = completedAt,
)

fun Chain.toMap(): Map<String, Any?> = mapOf(
    ChainFields.ID to id,
    ChainFields.CREATED_AT to createdAt.toTimestamp(),
    ChainFields.STATUS to status.name,
    ChainFields.GENERATION to generation,
    ChainFields.MAX_GENERATIONS to maxGenerations,
    ChainFields.TEXT_TIME_LIMIT to textTimeLimit,
    ChainFields.DRAWING_TIME_LIMIT to drawingTimeLimit,
    ChainFields.COMPLETED_AT to completedAt?.toTimestamp(),
)
