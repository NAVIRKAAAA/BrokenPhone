package com.brokentelephone.game.main.activity.model

import com.brokentelephone.game.domain.model.banner.BannerType
import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.nav_api.NavigationRoute

data class MainState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: Language = Language.ENGLISH,
    val startDestination: NavigationRoute? = null,
    val pendingRoutes: List<NavigationRoute> = emptyList(),
    val pendingNotificationId: String? = null,
    val sessionDataError: String? = null,
    val isSessionLoading: Boolean = false,
    val isLoading: Boolean = false,
    val currentBanner: BannerType? = null,
    val isBannerLoading: Boolean = false,
) {
    val isReady: Boolean get() = startDestination != null && pendingRoutes.isEmpty()
}
