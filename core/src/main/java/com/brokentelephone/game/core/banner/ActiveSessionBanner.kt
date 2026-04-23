package com.brokentelephone.game.core.banner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.brokentelephone.game.core.modifier.coloredShadow
import com.brokentelephone.game.core.swipe.swipeToDismiss
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
    val bannerShape = RoundedCornerShape(20.dp)
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .swipeToDismiss(enabled = visible, onDismiss = onDismiss),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .coloredShadow(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.14f),
                        blurRadius = 16f,
                        offsetY = 0.dp,
                        offsetX = 0.dp,
                        shape = bannerShape,
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = bannerShape,
                    )
                    .clip(bannerShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = !isLoading,
                        onClick = onContinueClick,
                    )
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp, bottom = 10.dp),
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
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(3.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActiveSessionBannerPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
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
