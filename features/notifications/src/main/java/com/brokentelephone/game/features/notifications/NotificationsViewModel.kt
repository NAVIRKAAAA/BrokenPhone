package com.brokentelephone.game.features.notifications

import androidx.lifecycle.ViewModel
import com.brokentelephone.game.features.notifications.model.NotificationsSideEffect
import com.brokentelephone.game.features.notifications.model.NotificationsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class NotificationsViewModel : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<NotificationsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()
}
