package com.broken.telephone.features.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.domain.settings.Language
import com.broken.telephone.features.app_preferences.use_case.GetLanguageUseCase
import com.broken.telephone.features.language.model.LanguageState
import com.broken.telephone.features.language.use_case.UpdateLanguageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LanguageState())
    val state = _state.asStateFlow()

    init {
        getLanguageUseCase()
            .onEach { language -> _state.update { it.copy(selectedLanguage = language) } }
            .launchIn(viewModelScope)
    }

    fun onLanguageClick(language: Language) {
        viewModelScope.launch {
            updateLanguageUseCase(language)
        }
    }
}
