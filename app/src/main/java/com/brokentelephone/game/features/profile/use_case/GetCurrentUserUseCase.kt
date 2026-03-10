package com.brokentelephone.game.features.profile.use_case

import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.features.profile.model.UserUi
import com.brokentelephone.game.features.profile.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCurrentUserUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<UserUi?> {
        return userSession.authState.map { authState ->
            authState.getUserOrNull()?.toUi()
        }
    }
}
