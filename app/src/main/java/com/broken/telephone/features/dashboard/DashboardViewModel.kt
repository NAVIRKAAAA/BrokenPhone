package com.broken.telephone.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.dashboard.model.DashboardState
import com.broken.telephone.features.dashboard.use_case.GetPostsUseCase
import com.broken.telephone.features.profile.use_case.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DashboardViewModel(
    private val getPostsUseCase: GetPostsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        getPostsUseCase()
            .onEach { posts -> _state.update { it.copy(posts = posts) } }
            .launchIn(viewModelScope)

        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(user = user) } }
            .launchIn(viewModelScope)
    }

}
