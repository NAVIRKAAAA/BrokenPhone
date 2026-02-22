package com.broken.telephone.features.app_preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.app_preferences.model.AppPreferencesState
import com.broken.telephone.features.app_preferences.use_case.GetLanguageUseCase
import com.broken.telephone.features.app_preferences.use_case.GetThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class AppPreferencesViewModel(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getThemeUseCase: GetThemeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AppPreferencesState())
    val state = _state.asStateFlow()

    init {
        getLanguageUseCase()
            .onEach { language -> _state.update { it.copy(language = language) } }
            .launchIn(viewModelScope)

        getThemeUseCase()
            .onEach { theme -> _state.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)
    }
}
