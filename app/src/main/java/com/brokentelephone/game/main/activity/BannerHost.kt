package com.brokentelephone.game.main.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brokentelephone.game.core.composable.banner.ActiveSessionBanner
import com.brokentelephone.game.core.composable.banner.NewsBanner
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
        remainingSeconds = activeSession?.remainingSeconds ?: 0,
        totalSeconds = activeSession?.totalSeconds ?: 1,
        isLoading = isLoading,
        onContinueClick = onContinueClick,
        onDismiss = onDismiss,
        modifier = modifier,
    )

    val newsNotification = currentBanner as? BannerType.NewsNotification
    NewsBanner(
        visible = newsNotification != null,
        title = newsNotification?.title.orEmpty(),
        body = newsNotification?.body.orEmpty(),
        remainingSeconds = newsNotification?.remainingSeconds ?: 0,
        totalSeconds = newsNotification?.totalSeconds ?: 1,
        onDismiss = onDismiss,
        onClick = onContinueClick,
        modifier = modifier,
    )
}
