package com.brokentelephone.game.features.notification_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.shimmer.ShimmerContent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberFormattedDate
import com.brokentelephone.game.features.notification_details.model.NotificationDetailsState

@Composable
fun NotificationDetailsContent(
    state: NotificationDetailsState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val notification = state.notificationUi

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        NotificationDetailsTopBar(
            title = notification?.title,
            onBackClick = onBackClick,
        )

        ShimmerContent(
            isLoading = notification == null,
            shimmerContent = { NotificationDetailsContentShimmer() },
            content = {
                if (notification != null) {
                    val formattedDate = rememberFormattedDate(notification.createdAt)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = notification.body,
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            text = formattedDate,
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        )


    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationDetailsContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        NotificationDetailsContent(
            state = NotificationDetailsState(
//                notificationUi = NotificationUi.News(
//                    id = "1",
//                    createdAt = System.currentTimeMillis(),
//                    isRead = false,
//                    title = "New feature dropped",
//                    body = "We just released a bunch of cool updates — new brush types, improved chain sharing, and a redesigned profile page. Make sure to update the app to get access to all the new features. We've also fixed a number of bugs reported by the community. Thank you for your feedback and support!",
//                ),
            ),
            onBackClick = {},
        )
    }
}
