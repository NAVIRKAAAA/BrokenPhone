package com.brokentelephone.game.core.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.shimmer.ShimmerContent

@Composable
fun ProfilePostsPage(
    posts: List<PostUi>,
    isLoading: Boolean,
    nestedScrollConnection: NestedScrollConnection,
    modifier: Modifier = Modifier,
    itemContent: @Composable (postUi: PostUi) -> Unit,
    emptyContent: @Composable (() -> Unit) = {},
) {

    ShimmerContent(
        isLoading = isLoading,
        shimmerContent = {
            ProfilePageShimmerList(modifier = modifier)
        },
        isEmpty = !isLoading && posts.isEmpty(),
        emptyContent = emptyContent,
        content = {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = posts,
                    key = { it.id }
                ) { post ->

                    itemContent(post)
                }

                item {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        },
    )
}
