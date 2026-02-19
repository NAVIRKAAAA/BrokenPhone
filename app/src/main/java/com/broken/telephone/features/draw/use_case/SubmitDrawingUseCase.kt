package com.broken.telephone.features.draw.use_case

import com.broken.telephone.domain.post.PostChainEntry
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.domain.repository.PostRepository
import kotlinx.coroutines.flow.first

class SubmitDrawingUseCase(
    private val repository: PostRepository,
) {

    suspend operator fun invoke(postId: String, localPath: String) {
        val post = repository.getPostById(postId).first() ?: return
        val updatedPost = post.copy(
            generation = post.generation + 1,
            currentEntry = PostChainEntry(
                authorId = "current_user",
                authorName = "Me",
                avatarUrl = null,
                content = PostContent.Drawing(localPath = localPath),
                createdAt = System.currentTimeMillis(),
                status = PostStatus.AVAILABLE,
                lockedBy = null,
            )
        )
        repository.updatePost(updatedPost)
    }
}
