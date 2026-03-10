package com.brokentelephone.game.features.bottom_nav_bar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import com.brokentelephone.game.features.bottom_nav_bar.model.BottomNavBar
import com.brokentelephone.game.features.bottom_nav_bar.model.BottomNavBarEvent
import com.brokentelephone.game.features.bottom_nav_bar.model.BottomNavBarState
import com.brokentelephone.game.features.profile.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.features.settings.use_case.GetAuthStateUseCase
import com.brokentelephone.game.navigation.routes.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppNavBottomBarViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(BottomNavBarState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<BottomNavBarEvent>()
    val event = _event.asSharedFlow()

    init {
        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(userAvatarUrl = user?.avatarUrl) } }
            .launchIn(viewModelScope)

        getAuthStateUseCase()
            .onEach { authState -> _state.update { it.copy(isAuth = authState.isAuth()) } }
            .launchIn(viewModelScope)
    }

    fun shouldShowBottomBar(entry: NavBackStackEntry): Boolean {
        return entry.destination.hasRoute<Routes.Dashboard>() ||
                entry.destination.hasRoute<Routes.Profile>()
    }

    fun updateSelectedItem(entry: NavBackStackEntry) {
        val item = when {
            entry.destination.hasRoute<Routes.Profile>() -> BottomNavBar.PROFILE
            else -> BottomNavBar.DASHBOARD
        }
        _state.update { it.copy(selectedItem = item) }
    }

    fun onScrollDirectionChange(isScrollingUp: Boolean) {
        _state.update { it.copy(isVisibleByScroll = isScrollingUp) }
    }

    fun resetScrollVisibility() {
        _state.update { it.copy(isVisibleByScroll = true) }
    }

    fun onItemClick(bottomNavBar: BottomNavBar) {
        viewModelScope.launch {
            val event = when(bottomNavBar) {
                BottomNavBar.DASHBOARD -> BottomNavBarEvent.NavigateToDashboard
                BottomNavBar.CREATE -> BottomNavBarEvent.NavigateToCreate
                BottomNavBar.PROFILE -> BottomNavBarEvent.NavigateToProfile
            }

            _event.emit(event)
        }
    }
}