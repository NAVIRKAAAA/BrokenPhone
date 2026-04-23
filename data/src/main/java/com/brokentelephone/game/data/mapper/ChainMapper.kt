package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.ChainDto
import com.brokentelephone.game.domain.model.chain.Chain
import com.brokentelephone.game.domain.model.chain.ChainStatus

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