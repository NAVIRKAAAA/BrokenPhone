package com.brokentelephone.game.features.notifications.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
fun NotificationsDateHeaderShimmer(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = "Сьогодні",
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 13.sp,
            lineHeight = 13.sp,
            modifier = Modifier.shimmer(cornerRadius = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsDateHeaderShimmerPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NotificationsDateHeaderShimmer()
        }
    }
}
