package com.brokentelephone.game.features.dashboard.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.model.post.PostContent

private val shimmerItems = listOf(
    PostContent.Text(""),
    PostContent.Drawing(),
    PostContent.Text(""),
    PostContent.Drawing(),
    PostContent.Text(""),
)

@Composable
fun DashboardShimmerList(
    modifier: Modifier = Modifier,
) {
    val items = remember { shimmerItems }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { content ->
            PostElementShimmer(
                content = content,
            )
        }

        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Preview
@Composable
fun DashboardShimmerListPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        DashboardShimmerList(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
