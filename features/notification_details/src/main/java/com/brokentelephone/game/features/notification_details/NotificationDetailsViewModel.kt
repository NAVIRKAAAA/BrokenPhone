package com.brokentelephone.game.features.notification_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.notification.NotificationUi
import com.brokentelephone.game.core.model.notification.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.use_case.GetNotificationByIdUseCase
import com.brokentelephone.game.features.notification_details.model.NotificationDetailsSideEffect
import com.brokentelephone.game.features.notification_details.model.NotificationDetailsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationDetailsViewModel(
    private val notificationId: String,
    private val getNotificationByIdUseCase: GetNotificationByIdUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationDetailsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<NotificationDetailsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        loadNotification()
    }

    private fun loadNotification() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(150)
            getNotificationByIdUseCase.execute(notificationId).onSuccess { result ->

                val notificationUi = result?.toUi()

                if (notificationUi != null && notificationUi is NotificationUi.News) {
                    _state.update { it.copy(isLoading = false, notificationUi = notificationUi) }
                } else {
                    // TODO: HANDLE
                }

            }.onError {
                // TODO: HANDLE
            }
        }
    }
}
