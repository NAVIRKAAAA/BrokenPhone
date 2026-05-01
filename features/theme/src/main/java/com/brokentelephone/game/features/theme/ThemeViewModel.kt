package com.brokentelephone.game.features.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.domain.use_case.GetThemeUseCase
import com.brokentelephone.game.features.theme.model.ThemeState
import com.brokentelephone.game.features.theme.use_case.UpdateThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ThemeState())
    val state = _state.asStateFlow()

    init {
        getThemeUseCase()
            .onEach { theme -> _state.update { it.copy(selectedTheme = theme) } }
            .launchIn(viewModelScope)
    }

    fun onThemeClick(theme: AppTheme) {
        viewModelScope.launch {
            updateThemeUseCase(theme)
        }
    }
}
