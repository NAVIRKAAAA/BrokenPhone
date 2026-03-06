package com.broken.telephone.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.app_preferences.use_case.GetThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val getThemeUseCase: GetThemeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        getThemeUseCase()
            .onEach { theme -> _state.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)
    }
}
