package com.broken.telephone.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.app_preferences.use_case.GetLanguageUseCase
import com.broken.telephone.features.app_preferences.use_case.GetThemeUseCase
import com.broken.telephone.features.language.use_case.InitializeLanguageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val initializeLanguageUseCase: InitializeLanguageUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        getThemeUseCase()
            .onEach { theme -> _state.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)

        getLanguageUseCase()
            .onEach { language -> _state.update { it.copy(language = language) } }
            .launchIn(viewModelScope)
    }

    fun initializeDefaultLanguage(deviceLanguageTag: String) {
        viewModelScope.launch {
            initializeLanguageUseCase(deviceLanguageTag)
        }
    }
}
