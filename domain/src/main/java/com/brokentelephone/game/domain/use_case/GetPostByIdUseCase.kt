package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetPostByIdUseCase(
    private val postRepository: PostRepository,
    private val usersRepository: UsersRepository,
    private val handler: ApiHandler,
) {

    fun execute(id: String): Flow<Post> {
        return postRepository.getPostById(id)
            .map { post ->
                val author = usersRepository.getUserById(post.authorId)
                if (author != null) {
                    post.copy(authorName = author.username, avatarUrl = author.avatarUrl)
                } else {
                    post
                }
            }
            .flowOn(Dispatchers.IO)
    }

    suspend fun executeWithResult(id: String): AppResult<Post> {
        return handler.handle(Dispatchers.IO) {
            postRepository.getPostById(id).firstOrNull() ?: throw PostNotFoundException()
        }
    }
}
