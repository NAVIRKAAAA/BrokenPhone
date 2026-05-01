package com.brokentelephone.game.core.composable.color_picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.ext.modifier.horizontalFadingEdge
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

val brushColors = listOf(
    Color(0xFF000000), // Black
    Color(0xFF6B7280), // Gray
    Color(0xFF2563EB), // Blue
    Color(0xFFFFFFFF), // White
    Color(0xFFD1D5DB), // Light gray
    Color(0xFF38BDF8), // Sky blue
    Color(0xFF166534), // Dark green
    Color(0xFF991B1B), // Dark red
    Color(0xFF92400E), // Brown
    Color(0xFF22C55E), // Green
    Color(0xFFEF4444), // Red
    Color(0xFFF97316), // Orange
    Color(0xFFB45309), // Golden brown
    Color(0xFF7C3AED), // Purple
    Color(0xFFFDA4AF), // Salmon
    Color(0xFFEAB308), // Yellow
    Color(0xFFEC4899), // Hot pink
    Color(0xFFFDE8E8), // Light pink
)

@Composable
fun BrushColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.horizontalFadingEdge(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(brushColors) { color ->
            ColorCircle(
                color = color,
                isSelected = color == selectedColor,
                onClick = { onColorSelected(color) },
            )
        }
    }
}

@Composable
private fun ColorCircle(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = tween(150),
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.5.dp else 1.dp,
        animationSpec = tween(150),
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .border(width = borderWidth, color = borderColor, shape = CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        val checkColor = if (color.luminance() > 0.5f) Color.Black else Color.White
        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(animationSpec = tween(150)),
            exit = scaleOut(animationSpec = tween(150)),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = checkColor,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BrushColorPickerPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        BrushColorPicker(
            selectedColor = Color.Black,
            onColorSelected = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BrushColorPickerDarkPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        BrushColorPicker(
            selectedColor = Color(0xFF3B82F6),
            onColorSelected = {},
        )
    }
}
