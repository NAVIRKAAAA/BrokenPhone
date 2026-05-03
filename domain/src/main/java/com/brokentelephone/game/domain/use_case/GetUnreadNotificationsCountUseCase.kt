package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest

class GetUnreadNotificationsCountUseCase(
    private val repository: NotificationsRepository,
    private val userSession: UserSession,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<Int> {
        return userSession.user
            .filterNotNull()
            .filter { userSession.authState.firstOrNull() is AuthState.Auth }
            .distinctUntilChangedBy { it.readNotificationIds }
            .flatMapLatest { user ->
                repository.getUnreadNotificationsCount(user.id, user.readNotificationIds)
            }
    }
}
