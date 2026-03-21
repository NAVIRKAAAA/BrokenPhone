package com.brokentelephone.game.core.badge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun BadgeElement(
    iconResId: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .clickable(onClick = onClick, enabled = enabled)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    }

}

@Composable
fun BadgeElement(
    iconResId: Int,
    text: String,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    }

}

@Preview
@Composable
fun BadgeElementPreview() {
    BrokenTelephoneTheme() {
        BadgeElement(
            R.drawable.ic_mutations,
            text = "0/10",
            onClick = {},
        )
    }
}