package com.brokentelephone.game.features.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.features.welcome.model.WelcomeSideEffect
import com.brokentelephone.game.features.welcome.use_case.ContinueAsGuestUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val continueAsGuestUseCase: ContinueAsGuestUseCase,
) : ViewModel() {

    private val _sideEffects = Channel<WelcomeSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onContinueAsGuest() {
        viewModelScope.launch {
            continueAsGuestUseCase()
            _sideEffects.send(WelcomeSideEffect.NavigateToDashboard)
        }
    }
}
