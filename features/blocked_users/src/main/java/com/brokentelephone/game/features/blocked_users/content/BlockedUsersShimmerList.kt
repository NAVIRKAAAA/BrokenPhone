package com.brokentelephone.game.features.blocked_users.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors

private val shimmerItems = List(6) { it }

@Composable
fun BlockedUsersShimmerList(
    modifier: Modifier = Modifier,
) {
    val items = remember { shimmerItems }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        userScrollEnabled = true,
    ) {
        itemsIndexed(items) { index, _ ->
            Column {
                if (index != 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                BlockedUserItemShimmer(
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                if (index != items.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.appColors.divider)
                }
            }
        }
    }
}

@Preview
@Composable
private fun BlockedUsersShimmerListPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        BlockedUsersShimmerList(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
