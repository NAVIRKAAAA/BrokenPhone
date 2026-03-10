package com.brokentelephone.game.features.create_post.use_case

import com.brokentelephone.game.domain.post.Post
import com.brokentelephone.game.domain.post.PostChainEntry
import com.brokentelephone.game.domain.post.PostContent
import com.brokentelephone.game.domain.post.PostStatus
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.UserSession
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
        val user = authState.getUserOrNull() ?: return

        val postId = System.currentTimeMillis().toString()
        val post = Post(
            id = postId,
            authorId = user.id,
            authorName = user.username,
            avatarUrl = user.avatarUrl,
            createdAt = System.currentTimeMillis(),
            generation = 1,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
            currentEntry = PostChainEntry(
                parentId = postId,
                authorId = user.id,
                authorName = user.username,
                avatarUrl = user.avatarUrl,
                content = PostContent.Text(text = text),
                createdAt = System.currentTimeMillis(),
                status = PostStatus.AVAILABLE,
                lockedBy = null,
            )
        )
        delay(1500)
        repository.createPost(post)
    }
}
