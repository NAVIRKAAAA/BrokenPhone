package com.broken.telephone.features.create_post.use_case

import com.broken.telephone.domain.post.Post
import com.broken.telephone.domain.post.PostChainEntry
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.domain.repository.PostRepository
import kotlinx.coroutines.delay

class CreatePostUseCase(
    private val repository: PostRepository,
) {

    suspend operator fun invoke(
        text: String,
        maxGenerations: Int,
        textTimeLimit: Int,
        drawingTimeLimit: Int,
    ) {
        val postId = System.currentTimeMillis().toString()
        val post = Post(
            id = postId,
            authorId = "current_user",
            authorName = "Me",
            avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_4.png",
            createdAt = System.currentTimeMillis(),
            generation = 0,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
            currentEntry = PostChainEntry(
                parentId = postId,
                authorId = "current_user",
                authorName = "Me",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_4.png",
                content = PostContent.Text(text = text, timeLimit = textTimeLimit),
                createdAt = System.currentTimeMillis(),
                status = PostStatus.AVAILABLE,
                lockedBy = null,
            )
        )
        delay(1500)
        repository.createPost(post)
    }
}
