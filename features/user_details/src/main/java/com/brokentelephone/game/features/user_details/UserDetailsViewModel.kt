package com.brokentelephone.game.features.user_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.user_details.model.UserDetailsState
import com.brokentelephone.game.features.user_details.use_case.GetUserByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val userId: String,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(UserDetailsState())
    val state = _state.asStateFlow()

    private var lastLoadedAt: Long = 0L

    init {
        loadUser()
    }

    fun onResume() {
        if (!isLoadAllowed(lastLoadedAt)) return

        viewModelScope.launch {
            fetchUser()
        }
    }

    private suspend fun fetchUser() {
        _state.update { it.copy(isUserLoading = true) }

        getUserByIdUseCase.execute(userId)
            .onSuccess { user ->
                _state.update { it.copy(isUserLoading = false, user = user?.toUi()) }
            }
            .onError { exception ->
                _state.update {
                    it.copy(
                        isUserLoading = false,
                        globalError = exceptionToMessageMapper.map(exception),
                    )
                }
            }
    }

    private fun isLoadAllowed(lastLoadedAt: Long): Boolean {
        if (lastLoadedAt == 0L) return true
        return System.currentTimeMillis() - lastLoadedAt >= COOLDOWN_MS
    }

    private fun loadUser() {
        viewModelScope.launch {

        }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null) }
    }

    private companion object {
        const val COOLDOWN_MS = 30_000L
    }
}
