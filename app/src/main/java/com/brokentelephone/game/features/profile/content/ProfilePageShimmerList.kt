package com.brokentelephone.game.features.profile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.brokentelephone.game.domain.model.post.PostContent

private val shimmerItems = listOf(
    PostContent.Text(""),
    PostContent.Drawing(),
    PostContent.Text(""),
    PostContent.Drawing(),
    PostContent.Text(""),
)

@Composable
fun ProfilePageShimmerList(
    modifier: Modifier = Modifier,
) {
    val items = remember { shimmerItems }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        userScrollEnabled = false,
    ) {
        itemsIndexed(items) { index, content ->
            if (index != 0) {
                HorizontalDivider(
                    color = MaterialTheme.appColors.divider,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            ProfilePostElementShimmer(
                content = content,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Preview
@Composable
private fun ProfilePageShimmerListPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        ProfilePageShimmerList(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
