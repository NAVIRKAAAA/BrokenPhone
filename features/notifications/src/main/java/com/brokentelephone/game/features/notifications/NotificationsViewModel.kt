package com.brokentelephone.game.features.notifications

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.notification.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.features.notifications.model.NotificationsSideEffect
import com.brokentelephone.game.features.notifications.model.NotificationsState
import com.brokentelephone.game.features.notifications.model.groupByDate
import com.brokentelephone.game.features.notifications.use_case.GetNotificationsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<NotificationsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()


    fun onResume() {
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

    private suspend fun fetchNotifications() {
        getNotificationsUseCase.execute().onSuccess { result ->
            _state.update { state ->
                state.copy(groupedNotifications = result.map { it.toUi() }.groupByDate())
            }
        }.onError { e ->
            Log.d("LOG_TAG", "fetchNotifications: $e")
        }
    }

}
