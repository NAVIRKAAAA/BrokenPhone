package com.brokentelephone.game.features.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.features.notifications.model.NotificationsSideEffect
import com.brokentelephone.game.features.notifications.model.NotificationsState
import com.brokentelephone.game.features.notifications.use_case.GetNotificationsUseCase
import com.brokentelephone.game.features.notifications.use_case.UpdateNotificationsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val updateNotificationsUseCase: UpdateNotificationsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<NotificationsSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getNotificationsUseCase()
            .onEach { notifications -> _state.update { it.copy(notifications = notifications) } }
            .launchIn(viewModelScope)
    }

    fun checkPermission(isGranted: Boolean) {
        _state.update { it.copy(isNotificationPermissionGranted = isGranted) }
    }

    fun onNotificationPermissionClick(isGranted: Boolean, shouldShowRationale: Boolean) {
        viewModelScope.launch {
            when {
                isGranted -> _sideEffects.send(NotificationsSideEffect.OpenNotificationSettings)
                shouldShowRationale -> _state.update { it.copy(showRationaleDialog = true) }
                else -> _sideEffects.send(NotificationsSideEffect.RequestPermission)
            }
        }
    }

    fun onRationaleConfirm() {
        _state.update { it.copy(showRationaleDialog = false) }
        viewModelScope.launch {
            _sideEffects.send(NotificationsSideEffect.RequestPermission)
        }
    }

    fun onRationaleDismiss() {
        _state.update { it.copy(showRationaleDialog = false) }
    }

    fun onAllNotificationsToggle(enabled: Boolean) {
        val updated = if (enabled) NotificationType.entries.toList() else emptyList()
        viewModelScope.launch {
            updateNotificationsUseCase(updated)
        }
    }

    fun onNotificationToggle(type: NotificationType, enabled: Boolean) {
        val current = _state.value.notifications
        val updated = if (enabled) current + type else current - type
        viewModelScope.launch {
            updateNotificationsUseCase(updated)
        }
    }
}
