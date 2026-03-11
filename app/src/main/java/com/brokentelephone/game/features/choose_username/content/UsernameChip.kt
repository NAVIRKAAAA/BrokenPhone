package com.brokentelephone.game.features.choose_username.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun UsernameChip(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
    ) {
        Text(
            text = name,
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
    }
}

@Preview
@Composable
fun UsernameChipPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            UsernameChip(
                name = "Doodle Gremlin",
                onClick = {},
                enabled = true
            )
        }
    }
}
