package com.broken.telephone.features.post_details.use_case

import com.broken.telephone.domain.repository.GamesRepository
import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.flow.first

class MockStartGameUseCase(
    private val gamesRepository: GamesRepository,
    private val postsRepository: PostRepository,
    private val userSession: UserSession,
) {

    /*

        1. create game in games repository
        2. update status in posts repository
        3. update game history in users repository

     */

    suspend operator fun invoke(postId: String) {
        val authState = userSession.authState.first()
        if (authState !is AuthState.Auth) return
        val userId = authState.user.id

        gamesRepository.createGame(postId, userId)
        // other firebase functions
    }
}
