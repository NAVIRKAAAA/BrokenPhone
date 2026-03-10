package com.brokentelephone.game.features.post_details.use_case

import com.brokentelephone.game.domain.repository.PostRepository

class DeletePostUseCase(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(postId: String) = repository.deletePost(postId)
}
