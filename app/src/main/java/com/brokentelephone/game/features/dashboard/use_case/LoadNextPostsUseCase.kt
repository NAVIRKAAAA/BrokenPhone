package com.brokentelephone.game.features.dashboard.use_case

import com.brokentelephone.game.data.model.PostsPage
import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.repository.PostRepository
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers

class LoadNextPostsUseCase(
    private val repository: PostRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(afterDoc: DocumentSnapshot, pageSize: Int): AppResult<PostsPage> {
        return handler.handle(Dispatchers.IO) {
            repository.loadNextPosts(afterDoc, pageSize)
        }
    }
}
