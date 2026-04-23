package com.brokentelephone.game.core.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R

@Composable
fun SettingsLogoutButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.nunito_bold)),
        fontSize = 15.sp,
        lineHeight = 22.sp,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsLogoutButtonPreview() {
    SettingsLogoutButton(onClick = {}, text = "Log Out")
}
