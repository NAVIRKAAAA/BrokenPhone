package com.brokentelephone.game.main

import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.model.session.GameSessionStatus
import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.navigation.routes.Routes

data class MainState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: Language = Language.ENGLISH,
    val startDestination: Routes? = null,
    val pendingRoutes: List<Routes> = emptyList(),
    val sessionDataError: String? = null,
    val isSessionLoading: Boolean = false,
    val isEmailChanging: Boolean = false,
    val activeSession: GameSession? = null,
    val bannerRemainingSeconds: Int = 0,
    val isBannerLoading: Boolean = false,
) {
    val isReady: Boolean get() = startDestination != null && pendingRoutes.isEmpty()

    val isBannerVisible: Boolean
        get() =
            activeSession != null &&
                    activeSession.status == GameSessionStatus.ACTIVE &&
                    bannerRemainingSeconds > 0

    val bannerFormattedTime: String
        get() = "%02d:%02d".format(
            bannerRemainingSeconds / 60,
            bannerRemainingSeconds % 60,
        )

    val bannerProgress: Float
        get() {
            val totalSeconds = activeSession
                ?.let { (it.expiresAt - it.lockedAt) / 1000f }
                ?.coerceAtLeast(1f)
                ?: 1f
            return (bannerRemainingSeconds / totalSeconds).coerceIn(0f, 1f)
        }
}
