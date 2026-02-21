package com.broken.telephone.features.dashboard.model

import com.broken.telephone.domain.post.Post
import com.broken.telephone.domain.post.PostContent

fun Post.toUi() = PostUi(
    id = id,
    parentId = currentEntry.parentId,
    authorId = currentEntry.authorId,
    authorName = authorName,
    avatarUrl = avatarUrl,
    content = when (val c = currentEntry.content) {
        is PostContent.Text -> c.copy(timeLimit = textTimeLimit)
        is PostContent.Drawing -> c.copy(timeLimit = drawingTimeLimit)
    },
    createdAt = createdAt,
    generation = generation,
    maxGenerations = maxGenerations,
    status = currentEntry.status
)
