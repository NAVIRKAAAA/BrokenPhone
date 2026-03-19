package com.brokentelephone.game.features.dashboard.model

import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent

fun Post.toUi() = PostUi(
    id = id,
    authorId = authorId,
    authorName = authorName,
    avatarUrl = avatarUrl,
    content = content,
    createdAt = updatedAt,
    generation = generation,
    maxGenerations = maxGenerations,
    status = status,
    nextTimeLimit = when (content) {
        is PostContent.Text -> drawingTimeLimit
        is PostContent.Drawing -> textTimeLimit
    },
)
