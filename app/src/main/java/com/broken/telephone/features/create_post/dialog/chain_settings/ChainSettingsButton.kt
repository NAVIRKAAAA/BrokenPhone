package com.broken.telephone.features.create_post.dialog.chain_settings

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme

enum class ChainSettingsButtonType(
    val value: String
) {
    PLUS("+"),
    MINUS("-")
}

@Composable
fun ChainSettingsButton(
    modifier: Modifier = Modifier,
    type: ChainSettingsButtonType = ChainSettingsButtonType.PLUS,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val color = if(enabled) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
            .copy(alpha = 0.38f)
    }

    Box(
        modifier = modifier
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false),
                onClick = onClick
            )
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
           ,
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = type.value,
            color = color,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.inter_medium))
        )

    }

}

@Preview
@Composable
fun ChainSettingsButtonPreview() {
    BrokenTelephoneTheme() {
        ChainSettingsButton(
            enabled = true
        ) {

        }
    }
}