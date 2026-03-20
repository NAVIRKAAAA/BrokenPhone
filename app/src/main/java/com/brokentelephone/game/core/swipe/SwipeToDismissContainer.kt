package com.brokentelephone.game.core.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import kotlin.math.abs

fun Modifier.swipeToDismiss(
    enabled: Boolean = true,
    horizontalThreshold: Float = 150f,
    upThreshold: Float = 80f,
    onDismiss: () -> Unit,
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(enabled) {
        if (enabled) {
            offsetX.snapTo(0f)
            offsetY.snapTo(0f)
            alpha.snapTo(1f)
        }
    }

    this
        .graphicsLayer {
            translationX = offsetX.value
            translationY = offsetY.value
            this.alpha = alpha.value
            rotationZ = offsetX.value / 40f
        }
        .pointerInput(enabled) {
            if (!enabled) return@pointerInput
            detectDragGestures(
                onDragEnd = {
                    coroutineScope.launch {
                        val dismissedHorizontally = abs(offsetX.value) > horizontalThreshold
                        val dismissedUp = offsetY.value < -upThreshold

                        if (dismissedHorizontally || dismissedUp) {
                            val targetX = if (dismissedHorizontally)
                                if (offsetX.value > 0) 800f else -800f
                            else offsetX.value
                            val targetY = if (dismissedUp) -800f else offsetY.value

                            launch { offsetX.animateTo(targetX, tween(200)) }
                            launch { offsetY.animateTo(targetY, tween(200)) }
                            launch { alpha.animateTo(0f, tween(150)) }.join()
                            onDismiss()
                        } else {
                            launch { offsetX.animateTo(0f, spring(dampingRatio = 0.5f, stiffness = 400f)) }
                            launch { offsetY.animateTo(0f, spring(dampingRatio = 0.5f, stiffness = 400f)) }
                        }
                    }
                },
                onDragCancel = {
                    coroutineScope.launch {
                        launch { offsetX.animateTo(0f, spring(dampingRatio = 0.5f, stiffness = 400f)) }
                        launch { offsetY.animateTo(0f, spring(dampingRatio = 0.5f, stiffness = 400f)) }
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    coroutineScope.launch {
                        offsetX.snapTo(offsetX.value + dragAmount.x)
                        offsetY.snapTo((offsetY.value + dragAmount.y).coerceAtMost(0f))
                    }
                },
            )
        }
}
