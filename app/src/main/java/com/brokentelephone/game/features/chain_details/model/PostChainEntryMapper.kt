package com.brokentelephone.game.features.chain_details.model

import com.brokentelephone.game.domain.model.post.PostChainEntry
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.features.dashboard.model.PostUi

fun PostChainEntry.toUi(
    id: String,
    generation: Int,
    maxGenerations: Int,
    textTimeLimit: Int,
    drawingTimeLimit: Int,
) = PostUi(
    id = id,
    parentId = parentId,
    authorId = authorId,
    authorName = authorName,
    avatarUrl = avatarUrl,
    content = content,
    createdAt = createdAt,
    generation = generation,
    maxGenerations = maxGenerations,
    status = status,
    nextTimeLimit = when (content) {
        is PostContent.Text -> drawingTimeLimit
        is PostContent.Drawing -> textTimeLimit
    },
)
