package com.broken.telephone.features.post_details.use_case

import com.broken.telephone.domain.repository.PostRepository

class DeletePostUseCase(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(postId: String) = repository.deletePost(postId)
}
