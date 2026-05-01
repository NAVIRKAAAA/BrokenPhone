package com.brokentelephone.game.core.composable.banner.content

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BannerProgressBar(
    remainingSeconds: Int,
    totalSeconds: Int,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val initialProgress = remember {
        remainingSeconds.toFloat() / totalSeconds.coerceAtLeast(1)
    }
    val animatable = remember { Animatable(initialProgress) }

    LaunchedEffect(remainingSeconds) {
        animatable.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = remainingSeconds * 1000,
                easing = LinearEasing,
            ),
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatable.value)
                .height(3.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.7f)),
        )
    }
}
