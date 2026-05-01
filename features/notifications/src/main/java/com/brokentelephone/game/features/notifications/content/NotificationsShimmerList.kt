package com.brokentelephone.game.features.notifications.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
private val shimmerGroups = listOf(
    listOf(0, 1, 2),
    listOf(2, 1, 2, 3),
)

@Composable
fun NotificationsShimmerList(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        shimmerGroups.forEach { items ->
            stickyHeader {
                NotificationsDateHeaderShimmer()
            }
            items(items.size) { _ ->
                ChainNotificationItemShimmer()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsShimmerListPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        NotificationsShimmerList(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
