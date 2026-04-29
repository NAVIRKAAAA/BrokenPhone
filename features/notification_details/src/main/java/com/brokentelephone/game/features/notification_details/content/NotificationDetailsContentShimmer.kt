package com.brokentelephone.game.features.notification_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.brokentelephone.game.core.composable.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun NotificationDetailsContentShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "We just released a bunch of cool updates — new brush types, improved chain sharing, and a redesigned profile page. Make sure to update the app.",
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 15.sp,
            lineHeight = 15.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.shimmer(cornerRadius = 4.dp),
        )
        Text(
            text = "Apr 8, 2026 · 2:30 PM",
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 12.sp,
            lineHeight = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .shimmer(cornerRadius = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationDetailsContentShimmerPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NotificationDetailsContentShimmer()
        }
    }
}
