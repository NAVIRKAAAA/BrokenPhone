package com.brokentelephone.game.features.notifications

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.notification.NotificationUi
import com.brokentelephone.game.core.model.notification.toUi
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.notification.NotificationFilter
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.features.notifications.model.NotificationsSideEffect
import com.brokentelephone.game.features.notifications.model.NotificationsState
import com.brokentelephone.game.features.notifications.model.groupByDate
import com.brokentelephone.game.features.notifications.use_case.GetNotificationsByFilterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsByFilterUseCase: GetNotificationsByFilterUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<NotificationsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var lastLoadedAt: Long = 0L

    init {
        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(user = user?.toUi()) } }
            .launchIn(viewModelScope)
    }

    fun onFilterSelected(filter: NotificationFilter) {
        viewModelScope.launch {
            _state.update { it.copy(selectedFilter = filter, isLoadingByFilter = true) }

            delay(150)

            fetchNotifications()

            _sideEffects.send(NotificationsSideEffect.ScrollToTop)

            _state.update { it.copy(isLoadingByFilter = false) }
        }
    }

    fun onResume() {
        if (!isLoadAllowed(lastLoadedAt)) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            fetchNotifications()

            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            delay(150)

            fetchNotifications()

            _state.update { it.copy(isRefreshing = false) }
        }
    }

    fun onNotificationClick(notification: NotificationUi) {
        viewModelScope.launch {
            when (notification) {
                is NotificationUi.Friends ->
                    _sideEffects.send(NotificationsSideEffect.NavigateToUserDetails(notification.userId))

                is NotificationUi.ChainInfo -> {
                    val userId = state.value.user?.id.orEmpty()
                    _sideEffects.send(
                        NotificationsSideEffect.NavigateToChainDetails(
                            notification.postId,
                            userId
                        )
                    )
                }

                is NotificationUi.News ->
                    _sideEffects.send(NotificationsSideEffect.NavigateToNotificationDetails(notification.id))
            }
        }
    }

    private suspend fun fetchNotifications() {
        val currentFilter = state.value.selectedFilter

        getNotificationsByFilterUseCase.execute(currentFilter).onSuccess { result ->
            _state.update { state ->
                state.copy(groupedNotifications = result.map { it.toUi() }.groupByDate())
            }

            lastLoadedAt = System.currentTimeMillis()
        }.onError { e ->
            Log.d("LOG_TAG", "fetchNotifications: $e")
        }
    }

    private fun isLoadAllowed(lastLoadedAt: Long): Boolean {
        if (lastLoadedAt == 0L) return true
        return System.currentTimeMillis() - lastLoadedAt >= COOLDOWN_MS
    }

    private companion object {
        const val COOLDOWN_MS = 30_000L
    }
}
