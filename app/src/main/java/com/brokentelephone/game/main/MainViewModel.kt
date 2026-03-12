package com.brokentelephone.game.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.handler.onError
import com.brokentelephone.game.domain.handler.onSuccess
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.essentials.exceptions.auth.SessionDataException
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.app_preferences.use_case.GetLanguageUseCase
import com.brokentelephone.game.features.app_preferences.use_case.GetThemeUseCase
import com.brokentelephone.game.features.language.use_case.InitializeLanguageUseCase
import com.brokentelephone.game.main.use_case.InitializeSessionUseCase
import com.brokentelephone.game.navigation.routes.Routes
import kotlinx.coroutines.delay
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
    private val initializeSessionUseCase: InitializeSessionUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        observeTheme()
        observeLanguage()
        initializeSession()
    }

    private fun observeTheme() {
        getThemeUseCase()
            .onEach { theme -> _state.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)
    }

    private fun observeLanguage() {
        getLanguageUseCase()
            .onEach { language -> _state.update { it.copy(language = language) } }
            .launchIn(viewModelScope)
    }

    fun initializeSession() {
        _state.update { it.copy(isSessionLoading = true) }
        viewModelScope.launch {
            initializeSessionUseCase.execute().onSuccess { authState ->
                val (destination, pendingRoutes) = when (authState.getUserOrNull()?.onboardingStep) {
                    null -> Routes.Welcome to emptyList()
                    OnboardingStep.CHOOSE_AVATAR -> Routes.ChooseAvatar to emptyList()
                    OnboardingStep.CHOOSE_USERNAME -> Routes.ChooseAvatar to listOf(Routes.ChooseUsername)
                    OnboardingStep.COMPLETED -> Routes.Dashboard to emptyList()
                }
                _state.update {
                    it.copy(
                        sessionDataError = null,
                        isSessionLoading = false,
                        startDestination = destination,
                        pendingRoutes = pendingRoutes
                    )
                }
            }.onError { error ->
                _state.update {
                    it.copy(
                        isSessionLoading = false,
                        startDestination = Routes.Welcome
                    )
                }

                if (error is SessionDataException) {
                    delay(150)
                    _state.update {
                        it.copy(
                            isSessionLoading = false,
                            sessionDataError = exceptionToMessageMapper.map(error)
                        )
                    }
                }
            }
        }
    }

    fun onSessionErrorDismissed() {
        _state.update { it.copy(sessionDataError = null, startDestination = Routes.Welcome) }
    }

    fun onPendingRoutesConsumed() {
        _state.update { it.copy(pendingRoutes = emptyList()) }
    }

    fun initializeDefaultLanguage(deviceLanguageTag: String) {
        viewModelScope.launch {
            initializeLanguageUseCase(deviceLanguageTag)
        }
    }
}
