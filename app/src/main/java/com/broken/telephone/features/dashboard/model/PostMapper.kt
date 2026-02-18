package com.broken.telephone.features.dashboard.model

import com.broken.telephone.domain.post.Post

fun Post.toUi() = PostUi(
    id = id,
    authorName = authorName,
    avatarUrl = avatarUrl,
    content = content,
    createdAt = createdAt,
    generation = generation,
    maxGenerations = maxGenerations,
    textTimeLimit = textTimeLimit,
    drawingTimeLimit = drawingTimeLimit,
)
