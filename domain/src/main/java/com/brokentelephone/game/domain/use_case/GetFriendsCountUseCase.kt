package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.FriendsRepository
import kotlinx.coroutines.Dispatchers

class GetFriendsCountUseCase(
    private val friendsRepository: FriendsRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(userId: String): AppResult<Int> {
        return handler.handle(Dispatchers.IO) {
            friendsRepository.getFriendsCount(userId)
        }
    }
}
