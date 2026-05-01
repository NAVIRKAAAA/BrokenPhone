package com.brokentelephone.game.core.composable.banner.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.composable.layout.swipe.swipeToDismiss
import com.brokentelephone.game.core.ext.modifier.coloredShadow

@Composable
fun BTBaseBanner(
    visible: Boolean,
    isLoading: Boolean,
    onContinueClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    shadowColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.14f),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .swipeToDismiss(enabled = visible, onDismiss = onDismiss),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .coloredShadow(
                    color = shadowColor,
                    blurRadius = 16f,
                    offsetY = 0.dp,
                    offsetX = 0.dp,
                    shape = shape,
                )
                .background(
                    color = backgroundColor,
                    shape = shape,
                )
                .clip(shape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = !isLoading,
                    onClick = onContinueClick,
                )
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 10.dp),
            content = content,
        )
    }
}
