package com.brokentelephone.game.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val badgeComplete: Color,
    val badgeCompleteContainer: Color,
    val badgeYou: Color,
    val divider: Color,
    val canvasBg: Color,
    val profileTabRowBg: Color,
    val newsBannerContainer: Color,
    val onNewsBannerContainer: Color,
    val successBannerContainer: Color,
    val onSuccessBannerContainer: Color,
)

val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        badgeComplete = Color.Unspecified,
        badgeCompleteContainer = Color.Unspecified,
        badgeYou = Color.Unspecified,
        divider = Color.Unspecified,
        canvasBg = Color.Unspecified,
        profileTabRowBg = Color.Unspecified,
        newsBannerContainer = Color.Unspecified,
        onNewsBannerContainer = Color.Unspecified,
        successBannerContainer = Color.Unspecified,
        onSuccessBannerContainer = Color.Unspecified,
    )
}

val MaterialTheme.appColors: AppColors
    @Composable get() = LocalAppColors.current
