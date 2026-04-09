package com.brokentelephone.game.core.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors

@Composable
fun PostChip(
    text: String,
    contentColor: Color,
    containerColor: Color,
    iconResId: Int?,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onClick: () -> Unit = {},
) {

    Row(
        modifier = modifier
            .clip(CircleShape)
            .clickable(enabled = enabled, onClick = onClick)
            .background(containerColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if(iconResId != null) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
            fontSize = 16.sp,
            lineHeight = 20.sp,
            color = contentColor
        )
    }
}

@Preview(name = "Finished chip", showBackground = true)
@Composable
private fun PostChipFinishedPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            PostChip(
                text = stringResource(R.string.dashboard_badge_complete),
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.appColors.badgeCompleteContainer,
                iconResId = R.drawable.ic_check,
            )
        }
    }
}

@Preview(name = "Generations chip", showBackground = true)
@Composable
private fun PostChipGenerationsPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            PostChip(
                text = "7/10",
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                iconResId = R.drawable.ic_mutations,
            )
        }
    }
}