package com.broken.telephone.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.profile.model.ProfileState
import com.broken.telephone.features.profile.model.ProfileTab
import com.broken.telephone.features.profile.use_case.GetCurrentUserUseCase
import com.broken.telephone.features.profile.use_case.GetMyContributionsUseCase
import com.broken.telephone.features.profile.use_case.GetMyPostsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getMyPostsUseCase: GetMyPostsUseCase,
    private val getMyContributionsUseCase: GetMyContributionsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _state.update { it.copy(user = user) }
            }
        }
        viewModelScope.launch {
            getMyPostsUseCase().collect { posts ->
                _state.update { it.copy(myPosts = posts) }
            }
        }
        viewModelScope.launch {
            getMyContributionsUseCase().collect { posts ->
                _state.update { it.copy(myContributions = posts) }
            }
        }
    }

    fun onTabSelect(tab: ProfileTab) {
        _state.update { it.copy(selectedTab = tab) }
    }
}
