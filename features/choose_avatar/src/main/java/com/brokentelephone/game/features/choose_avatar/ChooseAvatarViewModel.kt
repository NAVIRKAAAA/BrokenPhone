package com.brokentelephone.game.features.choose_avatar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.avatar.AvatarUi
import com.brokentelephone.game.core.avatar.Avatars
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.choose_avatar.model.ChooseAvatarEvent
import com.brokentelephone.game.features.choose_avatar.model.ChooseAvatarState
import com.brokentelephone.game.features.choose_avatar.use_case.CompleteAvatarStepUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChooseAvatarViewModel(
    private val completeAvatarStepUseCase: CompleteAvatarStepUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(ChooseAvatarState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ChooseAvatarEvent>()
    val event = _event.asSharedFlow()

    fun onAvatarClick(avatar: AvatarUi) {
        if (_state.value.loadingAvatarId != null) return

        viewModelScope.launch {
            _state.update { it.copy(loadingAvatarId = avatar.id) }
            val avatarUrl = Avatars.all.first { it.id == avatar.id }.url

            completeAvatarStepUseCase.execute(avatarUrl).onSuccess {
                _state.update { it.copy(loadingAvatarId = null) }
                _event.emit(ChooseAvatarEvent.NavigateToChooseUsername)
            }.onError { error ->
                _state.update { it.copy(loadingAvatarId = null, globalError = exceptionToMessageMapper.map(error)) }
            }
        }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }
}
