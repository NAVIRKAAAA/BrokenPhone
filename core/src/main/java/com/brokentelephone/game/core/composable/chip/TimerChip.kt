package com.brokentelephone.game.core.composable.chip

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.toFormattedTime

private enum class TimerStage { Normal, Urgent }

private fun timerStage(remainingSeconds: Int): TimerStage =
    if (remainingSeconds < 10) TimerStage.Urgent else TimerStage.Normal

@Composable
fun TimerChip(
    remainingSeconds: Int,
    modifier: Modifier = Modifier,
) {
    val stage = timerStage(remainingSeconds)

    val targetContainer = when (stage) {
        TimerStage.Normal -> MaterialTheme.colorScheme.primaryContainer
        TimerStage.Urgent -> MaterialTheme.colorScheme.errorContainer
    }
    val targetContent = when (stage) {
        TimerStage.Normal -> MaterialTheme.colorScheme.onPrimaryContainer
        TimerStage.Urgent -> MaterialTheme.colorScheme.onErrorContainer
    }

    val containerColor by animateColorAsState(
        targetValue = targetContainer,
        animationSpec = tween(durationMillis = 600),
        label = "timerContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = targetContent,
        animationSpec = tween(durationMillis = 600),
        label = "timerContent",
    )

    val infiniteTransition = rememberInfiniteTransition(label = "timerPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseScale",
    )
    val scale = if (stage == TimerStage.Urgent && remainingSeconds > 0) pulseScale else 1f

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(containerColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_clock),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = remainingSeconds.toFormattedTime(),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = contentColor,
            )
        }
    }
}

@Preview(name = "Normal", showBackground = true)
@Composable
private fun TimerChipNormalPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(Modifier.background(MaterialTheme.colorScheme.background).padding(16.dp)) {
            TimerChip(remainingSeconds = 45)
        }
    }
}

@Preview(name = "Urgent", showBackground = true)
@Composable
private fun TimerChipUrgentPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(Modifier.background(MaterialTheme.colorScheme.background).padding(16.dp)) {
            TimerChip(remainingSeconds = 4)
        }
    }
}
