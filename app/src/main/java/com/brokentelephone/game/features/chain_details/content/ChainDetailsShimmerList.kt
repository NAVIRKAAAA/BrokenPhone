package com.brokentelephone.game.features.chain_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.post.PostContent

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

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        userScrollEnabled = false,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        itemsIndexed(items) { index, content ->
            Column {
                if (index != 0) {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                ChainDetailsElementShimmer(content = content)

                if (index != items.lastIndex) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 52.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_down),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.shimmer(cornerRadius = 4.dp),
                        )
                    }
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
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        )
    }
}
