package com.brokentelephone.game.features.notifications_settings.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun NotificationSwitchItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onCheckedChange(!checked) }
            )
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors().copy(
                checkedBorderColor = Color.Transparent,
                uncheckedBorderColor = Color.Transparent,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationSwitchItemPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NotificationSwitchItem(
                text = "All Notifications",
                checked = true,
                onCheckedChange = {},
            )
        }
    }
}
