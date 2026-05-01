package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.PostDto
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus

fun PostDto.toPost(): Post = Post(
    id = id,
    chainId = chainId,
    authorId = authorId,
    authorName = author?.username ?: authorName,
    avatarUrl = author?.avatarUrl ?: avatarUrl,
    content = when (contentType) {
        "DRAWING" -> PostContent.Drawing(imageUrl = contentImageUrl)
        else -> PostContent.Text(text = contentText.orEmpty())
    },
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = PostStatus.fromString(status),
    generation = generation,
    maxGenerations = maxGenerations,
    textTimeLimit = textTimeLimit,
    drawingTimeLimit = drawingTimeLimit,
    chainSize = null
)

fun Post.toPostDto(): PostDto = PostDto(
    id = id,
    chainId = chainId,
    authorId = authorId,
    authorName = authorName,
    avatarUrl = avatarUrl,
    contentType = when (content) {
        is PostContent.Text -> "TEXT"
        is PostContent.Drawing -> "DRAWING"
    },
    contentText = (content as? PostContent.Text)?.text,
    contentImageUrl = (content as? PostContent.Drawing)?.imageUrl,
    status = status.name,
    generation = generation,
    maxGenerations = maxGenerations,
    textTimeLimit = textTimeLimit,
    drawingTimeLimit = drawingTimeLimit,
    createdAt = createdAt,
    updatedAt = updatedAt,
)