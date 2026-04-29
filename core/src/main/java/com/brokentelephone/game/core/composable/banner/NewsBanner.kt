package com.brokentelephone.game.core.composable.banner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors

@Composable
fun NewsBanner(
    visible: Boolean,
    title: String,
    body: String,
    progress: Float,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "newsProgress",
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier,
    ) {
        BTBaseBanner(
            visible = visible,
            isLoading = false,
            onContinueClick = onClick,
            onDismiss = onDismiss,
            backgroundColor = MaterialTheme.appColors.newsBannerContainer,
            shadowColor = MaterialTheme.appColors.newsBannerContainer.copy(alpha = 0.4f),
        ) {
            val contentColor = MaterialTheme.appColors.onNewsBannerContainer
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.1f)),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_megaphone),
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

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = contentColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(contentColor.copy(alpha = 0.15f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(3.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.7f)),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsBannerPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NewsBanner(
                visible = true,
                title = "New update available",
                body = "Check out the latest features we added to the app this week!",
                progress = 0.6f,
                onDismiss = {},
                onClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsBannerDarkPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NewsBanner(
                visible = true,
                title = "New update available",
                body = "Check out the latest features we added to the app this week!",
                progress = 0.6f,
                onDismiss = {},
                onClick = {},
            )
        }
    }
}
