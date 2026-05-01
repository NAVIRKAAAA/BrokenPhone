package com.brokentelephone.game.features.blocked_users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.blocked_users.model.BlockedUserUi
import com.brokentelephone.game.features.blocked_users.model.BlockedUsersSideEffect
import com.brokentelephone.game.features.blocked_users.model.BlockedUsersState
import com.brokentelephone.game.features.blocked_users.use_case.GetBlockedUsersUseCase
import com.brokentelephone.game.features.blocked_users.use_case.UnblockUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlockedUsersViewModel(
    private val getBlockedUsersUseCase: GetBlockedUsersUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(BlockedUsersState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<BlockedUsersSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onResume() {
        loadBlockedUsers()
    }

    private fun loadBlockedUsers() {
        Log.d("LOG_TAG", "loadBlockedUsers()")

        viewModelScope.launch {

            _state.update { it.copy(isLoading = true) }
            getBlockedUsersUseCase.execute().onSuccess { users ->
                Log.d("LOG_TAG", "loadBlockedUsers: onSuccess (${users.size})")
                _state.update {
                    it.copy(
                        blockedUsers = users,
                        isLoading = false,
                        isLoadRetrying = false,
                        loadError = null
                    )
                }
            }.onError { exception ->
                Log.d("LOG_TAG", "loadBlockedUsers: onError ($exception)")
                _state.update {
                    it.copy(
                        isLoading = false,
                        isLoadRetrying = false,
                        loadError = exceptionToMessageMapper.map(exception),
                    )
                }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            delay(150)

            getBlockedUsersUseCase.execute().onSuccess { users ->
                _state.update { it.copy(blockedUsers = users, isRefreshing = false) }
            }.onError { e ->
                _state.update {
                    it.copy(
                        isRefreshing = false,
                        globalError = exceptionToMessageMapper.map(e),
                    )
                }
            }
        }
    }

    fun onUnblockClick(user: BlockedUserUi) {
        _state.update { it.copy(unblockDialogUser = user) }
    }

    fun onUnblockDialogDismiss() {
        _state.update { it.copy(unblockDialogUser = null) }
    }

    fun onUnblockConfirm() {
        val blockId = _state.value.unblockDialogUser?.id ?: return
        _state.update { it.copy(isUnblockLoading = true) }
        viewModelScope.launch {
            unblockUserUseCase.execute(blockId).onSuccess {
                _state.update { it.copy(unblockDialogUser = null, isUnblockLoading = false) }
                loadBlockedUsers()
            }.onError { exception ->
                _state.update {
                    it.copy(
                        isUnblockLoading = false,
                        unblockDialogUser = null,
                        globalError = exceptionToMessageMapper.map(exception)
                    )
                }
            }
        }
    }

    fun onLoadErrorRetry() {
        _state.update { it.copy(isLoadRetrying = true) }
        loadBlockedUsers()
    }

    fun onLoadErrorDismiss() {
        _state.update { it.copy(loadError = null) }
        viewModelScope.launch {
            _sideEffects.send(BlockedUsersSideEffect.NavigateBack)
        }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null) }
    }
}
