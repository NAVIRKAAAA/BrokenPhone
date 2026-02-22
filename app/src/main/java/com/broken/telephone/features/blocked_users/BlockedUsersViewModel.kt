package com.broken.telephone.features.blocked_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.blocked_users.model.BlockedUserUi
import com.broken.telephone.features.blocked_users.model.BlockedUsersState
import com.broken.telephone.features.blocked_users.use_case.GetBlockedUsersUseCase
import com.broken.telephone.features.blocked_users.use_case.UnblockUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlockedUsersViewModel(
    private val getBlockedUsersUseCase: GetBlockedUsersUseCase,
    private val unblockUserUseCase: UnblockUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(BlockedUsersState())
    val state = _state.asStateFlow()

    init {
        getBlockedUsersUseCase()
            .onEach { users -> _state.update { it.copy(blockedUsers = users) } }
            .launchIn(viewModelScope)
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
}
