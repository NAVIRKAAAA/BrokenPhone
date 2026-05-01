package com.brokentelephone.game.features.chain_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    PostContent.Drawing(),
)

@Composable
fun ChainDetailsShimmerList(
    modifier: Modifier = Modifier,
) {
    val items = remember { shimmerItems }
    val listContentTopPadding =
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 80.dp

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = listContentTopPadding, bottom = 16.dp),
        userScrollEnabled = false,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(items) { index, content ->
            ChainDetailsElementShimmer(content = content)

            if (index != items.lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    VerticalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                        modifier = Modifier.padding(start = 32.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChainDetailsShimmerListPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        ChainDetailsShimmerList(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        )
    }
}
