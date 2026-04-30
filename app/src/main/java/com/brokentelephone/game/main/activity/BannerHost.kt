package com.brokentelephone.game.main.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brokentelephone.game.core.composable.banner.ActiveSessionBanner
import com.brokentelephone.game.domain.model.banner.BannerType

@Composable
fun BannerHost(
    currentBanner: BannerType?,
    isLoading: Boolean,
    onContinueClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val activeSession = currentBanner as? BannerType.ActiveSession
    ActiveSessionBanner(
        visible = activeSession != null,
        formattedTime = activeSession?.formattedTime.orEmpty(),
        progress = activeSession?.progress ?: 0f,
        isLoading = isLoading,
        onContinueClick = onContinueClick,
        onDismiss = onDismiss,
        modifier = modifier,
    )
}
