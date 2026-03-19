package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.ext.toMillis
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.model.chain.Chain
import com.brokentelephone.game.domain.model.chain.ChainStatus
import com.google.firebase.Timestamp

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

@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.toChain(): Chain? {
    return try {
        Chain(
            id = this[ChainFields.ID] as? String ?: return null,
            createdAt = (this[ChainFields.CREATED_AT] as? Timestamp)?.toMillis() ?: 0L,
            status = (this[ChainFields.STATUS] as? String)
                ?.let { runCatching { ChainStatus.valueOf(it) }.getOrNull() }
                ?: ChainStatus.ACTIVE,
            generation = (this[ChainFields.GENERATION] as? Long)?.toInt() ?: return null,
            maxGenerations = (this[ChainFields.MAX_GENERATIONS] as? Long)?.toInt() ?: return null,
            textTimeLimit = (this[ChainFields.TEXT_TIME_LIMIT] as? Long)?.toInt() ?: return null,
            drawingTimeLimit = (this[ChainFields.DRAWING_TIME_LIMIT] as? Long)?.toInt() ?: return null,
            completedAt = (this[ChainFields.COMPLETED_AT] as? Timestamp)?.toMillis(),
        )
    } catch (_: Exception) {
        null
    }
}
