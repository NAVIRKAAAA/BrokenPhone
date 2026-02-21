package com.broken.telephone.features.create_post.use_case

import com.broken.telephone.domain.post.Post
import com.broken.telephone.domain.post.PostChainEntry
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class CreatePostUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
) {

    suspend operator fun invoke(
        text: String,
        maxGenerations: Int,
        textTimeLimit: Int,
        drawingTimeLimit: Int,
    ) {
        val authState = userSession.authState.first()
        if (authState !is AuthState.Auth) return
        val user = authState.user

        val postId = System.currentTimeMillis().toString()
        val post = Post(
            id = postId,
            authorId = user.id,
            authorName = user.username,
            avatarUrl = user.avatarUrl,
            createdAt = System.currentTimeMillis(),
            generation = 0,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
            currentEntry = PostChainEntry(
                parentId = postId,
                authorId = user.id,
                authorName = user.username,
                avatarUrl = user.avatarUrl,
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
