package com.brokentelephone.game.core.composable.banner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.banner.content.BTBaseBanner
import com.brokentelephone.game.core.composable.banner.content.BannerProgressBar
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors

@Composable
fun SuccessBanner(
    visible: Boolean,
    title: String,
    body: String?,
    remainingSeconds: Int,
    totalSeconds: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier,
    ) {
        BTBaseBanner(
            visible = visible,
            isLoading = false,
            onContinueClick = onDismiss,
            onDismiss = onDismiss,
            backgroundColor = MaterialTheme.appColors.successBannerContainer,
            shadowColor = MaterialTheme.appColors.successBannerContainer.copy(alpha = 0.4f),
        ) {
            val contentColor = MaterialTheme.appColors.onSuccessBannerContainer

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.12f)),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (body != null) {
                        Text(
                            text = body,
                            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = contentColor.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            BannerProgressBar(
                remainingSeconds = remainingSeconds,
                totalSeconds = totalSeconds,
                color = contentColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccessBannerPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SuccessBanner(
                visible = true,
                title = "Drawing submitted!",
                body = "Waiting for the next player",
                remainingSeconds = 7,
                totalSeconds = 10,
                onDismiss = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccessBannerDarkPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SuccessBanner(
                visible = true,
                title = "Drawing submitted!",
                body = "Waiting for the next player",
                remainingSeconds = 7,
                totalSeconds = 10,
                onDismiss = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccessBannerNoBodyPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SuccessBanner(
                visible = true,
                title = "Friend request accepted!",
                body = null,
                remainingSeconds = 4,
                totalSeconds = 10,
                onDismiss = {},
            )
        }
    }
}
