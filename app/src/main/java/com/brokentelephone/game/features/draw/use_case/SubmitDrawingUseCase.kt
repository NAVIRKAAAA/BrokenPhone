package com.brokentelephone.game.features.draw.use_case

import com.brokentelephone.game.domain.post.PostChainEntry
import com.brokentelephone.game.domain.post.PostContent
import com.brokentelephone.game.domain.post.PostStatus
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class SubmitDrawingUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
) {

    suspend operator fun invoke(postId: String, localPath: String) {
        val authState = userSession.authState.first()
        val user = (authState as? AuthState.Auth)?.user ?: return
        val post = repository.getPostById(postId).first() ?: return

        delay(1500)

        val now = System.currentTimeMillis()
        val updatedPost = post.copy(
            generation = post.generation + 1,
            updatedAt = now,
            currentEntry = PostChainEntry(
                parentId = postId,
                authorId = user.id,
                authorName = user.username,
                avatarUrl = user.avatarUrl,
                content = PostContent.Drawing(localPath = localPath),
                createdAt = now,
                updatedAt = now,
                status = PostStatus.AVAILABLE,
            )
        )
        repository.updatePost(updatedPost)
    }
}
