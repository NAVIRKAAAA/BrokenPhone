package com.brokentelephone.game.features.notification_details

import androidx.lifecycle.ViewModel
import com.brokentelephone.game.features.notification_details.model.NotificationDetailsSideEffect
import com.brokentelephone.game.features.notification_details.model.NotificationDetailsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class NotificationDetailsViewModel(
    private val notificationId: String,
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationDetailsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<NotificationDetailsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()
}
