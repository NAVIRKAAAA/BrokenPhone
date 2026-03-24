package com.brokentelephone.game.features.user_details.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.User
import kotlinx.coroutines.Dispatchers

class GetUserByIdUseCase(
    private val usersRepository: UsersRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(userId: String): AppResult<User?> {
        return handler.handle(Dispatchers.IO) {
            usersRepository.getUserById(userId)
        }
    }
}
