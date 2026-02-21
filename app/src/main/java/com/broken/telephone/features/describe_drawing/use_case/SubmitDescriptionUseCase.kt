package com.broken.telephone.features.describe_drawing.use_case

import com.broken.telephone.domain.post.PostChainEntry
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.flow.first

class SubmitDescriptionUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
) {

    suspend operator fun invoke(postId: String, text: String) {
        val authState = userSession.authState.first()
        val user = (authState as? AuthState.Auth)?.user ?: return
        val post = repository.getPostById(postId).first() ?: return
        val updatedPost = post.copy(
            generation = post.generation + 1,
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
        repository.updatePost(updatedPost)
    }
}
