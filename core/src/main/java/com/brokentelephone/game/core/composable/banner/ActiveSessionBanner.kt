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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun ActiveSessionBanner(
    visible: Boolean,
    formattedTime: String,
    progress: Float,
    isLoading: Boolean,
    onContinueClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "bannerProgress",
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier,
    ) {
        BTBaseBanner(
            visible = visible,
            isLoading = isLoading,
            onContinueClick = onContinueClick,
            onDismiss = onDismiss,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.active_session_banner_title),
                        fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                    )
                    Text(
                        text = formattedTime,
                        fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                        fontSize = 28.sp,
                        lineHeight = 34.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp),
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_right),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(3.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActiveSessionBannerPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            ActiveSessionBanner(
                visible = true,
                formattedTime = "00:29",
                progress = 0.65f,
                isLoading = false,
                onContinueClick = {},
                onDismiss = {},
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}
