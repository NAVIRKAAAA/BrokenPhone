package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.User
import kotlinx.coroutines.Dispatchers

class GetUsersByIdsUseCase(
    private val usersRepository: UsersRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(ids: List<String>): AppResult<List<User>> {
        return handler.handle(Dispatchers.IO) {
            usersRepository.getUsersByIds(ids)
        }
    }
}
