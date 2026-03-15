package com.brokentelephone.game.features.blocked_users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.handler.onError
import com.brokentelephone.game.domain.handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.blocked_users.model.BlockedUserUi
import com.brokentelephone.game.features.blocked_users.model.BlockedUsersState
import com.brokentelephone.game.features.blocked_users.use_case.GetBlockedUsersUseCase
import com.brokentelephone.game.features.blocked_users.use_case.UnblockUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlockedUsersViewModel(
    private val getBlockedUsersUseCase: GetBlockedUsersUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(BlockedUsersState())
    val state = _state.asStateFlow()

    private var lastLoadedAt: Long = 0L

    fun onResume() {
        loadBlockedUsers()
    }

    private fun loadBlockedUsers() {
        if (!isInitialLoadAllowed()) return
        Log.d("LOG_TAG", "loadBlockedUsers()")

        viewModelScope.launch {
            getBlockedUsersUseCase.execute().onSuccess { users ->
                Log.d("LOG_TAG", "loadBlockedUsers: onSuccess (${users.size})")
                lastLoadedAt = System.currentTimeMillis()
                _state.update { it.copy(blockedUsers = users, isLoading = false) }
            }.onError { e ->
                Log.d("LOG_TAG", "loadBlockedUsers: onError ($e)")
                _state.update {
                    it.copy(
                        isLoading = false,
                        globalError = exceptionToMessageMapper.map(e),
                        globalException = e,
                    )
                }
            }
        }
    }

    private fun isInitialLoadAllowed(): Boolean {
        if (lastLoadedAt == 0L) return true
        return System.currentTimeMillis() - lastLoadedAt >= REFRESH_COOLDOWN_MS
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
            unblockUserUseCase(blockId)
            _state.update { it.copy(unblockDialogUser = null, isUnblockLoading = false) }
        }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null, globalException = null) }
    }

    private companion object {
        const val REFRESH_COOLDOWN_MS = 30_000L
    }
}
