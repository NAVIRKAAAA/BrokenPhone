package com.broken.telephone.features.dashboard.model

import com.broken.telephone.domain.post.Post
import com.broken.telephone.domain.post.PostContent

fun Post.toUi() = PostUi(
    id = id,
    parentId = currentEntry.parentId,
    authorId = currentEntry.authorId,
    authorName = authorName,
    avatarUrl = avatarUrl,
    content = currentEntry.content,
    createdAt = createdAt,
    generation = generation,
    maxGenerations = maxGenerations,
    status = currentEntry.status,
    nextTimeLimit = when (currentEntry.content) {
        is PostContent.Text -> drawingTimeLimit
        is PostContent.Drawing -> textTimeLimit
    },
)
