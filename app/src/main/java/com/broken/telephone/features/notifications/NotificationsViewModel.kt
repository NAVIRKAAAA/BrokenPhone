package com.broken.telephone.features.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.domain.settings.NotificationType
import com.broken.telephone.features.notifications.model.NotificationsState
import com.broken.telephone.features.notifications.use_case.GetNotificationsUseCase
import com.broken.telephone.features.notifications.use_case.UpdateNotificationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val updateNotificationsUseCase: UpdateNotificationsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NotificationsState())
    val state = _state.asStateFlow()

    init {
        getNotificationsUseCase()
            .onEach { notifications -> _state.update { it.copy(enabledNotifications = notifications) } }
            .launchIn(viewModelScope)
    }

    fun onAllNotificationsToggle(enabled: Boolean) {
        val updated = if (enabled) NotificationType.entries.toList() else emptyList()
        viewModelScope.launch {
            updateNotificationsUseCase(updated)
        }
    }

    fun onNotificationToggle(type: NotificationType, enabled: Boolean) {
        val current = _state.value.enabledNotifications
        val updated = if (enabled) current + type else current - type
        viewModelScope.launch {
            updateNotificationsUseCase(updated)
        }
    }
}
