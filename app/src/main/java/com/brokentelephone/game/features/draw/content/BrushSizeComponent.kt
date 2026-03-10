package com.brokentelephone.game.features.draw.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.draw.model.BrushSize

@Composable
fun BrushSizeComponent(
    selectedBrushSize: BrushSize,
    modifier: Modifier = Modifier,
    onClick: (BrushSize) -> Unit = {},
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        BrushSize.entries.forEach { item ->
            BrushSizeComponentSingle(
                brushSize = item,
                isSelected = selectedBrushSize == item,
                onClick = { onClick(item) }
            )
        }

    }

}

@Composable
fun BrushSizeComponentSingle(
    brushSize: BrushSize,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    val text = brushSize.name.first().uppercase()

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false),
                onClick = onClick
            )
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 14.sp,
            lineHeight = 24.sp,
            maxLines = 1,
            color = contentColor
        )

    }

}

@Preview
@Composable
fun BrushSizeComponentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            BrushSizeComponent(
                selectedBrushSize = BrushSize.MEDIUM
            )
        }
    }
}