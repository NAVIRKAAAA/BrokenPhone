package com.brokentelephone.game.core.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun StatInfo(
    value: Int,
    name: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier.clickable(
            onClick = onClick,
            enabled = enabled,
            indication = null,
            interactionSource = remember { MutableInteractionSource() })
    ) {
        Text(
            text = value.toString(),
            textAlign = TextAlign.Start,
            fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
            fontSize = 17.sp,
            lineHeight = 25.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = name,
            textAlign = TextAlign.Start,
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun StatInfoPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            StatInfo(value = 12, name = "Posts")
        }
    }
}
