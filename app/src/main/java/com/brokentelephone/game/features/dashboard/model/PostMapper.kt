package com.brokentelephone.game.features.dashboard.model

import com.brokentelephone.game.domain.post.Post
import com.brokentelephone.game.domain.post.PostContent

fun Post.toUi() = PostUi(
    id = id,
    parentId = parentId ?: id,
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
