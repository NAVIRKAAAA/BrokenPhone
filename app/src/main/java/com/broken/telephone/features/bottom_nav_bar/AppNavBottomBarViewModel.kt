package com.broken.telephone.features.bottom_nav_bar

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.broken.telephone.features.bottom_nav_bar.model.BottomNavBar
import com.broken.telephone.features.bottom_nav_bar.model.BottomNavBarEvent
import com.broken.telephone.features.bottom_nav_bar.model.BottomNavBarState
import com.broken.telephone.navigation.routes.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppNavBottomBarViewModel(

) : ViewModel() {

    private val _state = MutableStateFlow(BottomNavBarState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<BottomNavBarEvent>()
    val event = _event.asSharedFlow()

    init {
        Log.d("LOG_TAG", "Init AppNavBottomBarViewModel")
    }

    fun shouldShowBottomBar(entry: NavBackStackEntry): Boolean {
        return entry.destination.hasRoute<Routes.Dashboard>()
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