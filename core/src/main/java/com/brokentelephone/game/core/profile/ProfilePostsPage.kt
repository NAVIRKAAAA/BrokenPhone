package com.brokentelephone.game.core.profile

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.shimmer.ShimmerContent

@Composable
fun ProfilePostsPage(
    posts: List<PostUi>,
    tab: ProfileTab,
    isLoading: Boolean,
    nestedScrollConnection: NestedScrollConnection,
    onPostClick: (postId: String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
    emptyContent: @Composable (() -> Unit) = {}
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
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = posts,
                    key = { it.id }
                ) { post ->
                    if(tab == ProfileTab.POSTS) {
                        ProfilePostElement(
                            post = post,
                            onMoreClick = { onMoreClick(post.id) },
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        onPostClick(post.id)
                                    },
                                    onLongClick = {
                                        onMoreClick(post.id)
                                    },
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                )
                        )
                    } else {
                        ProfileContributionElement(
                            post = post,
                            onMoreClick = { onMoreClick(post.id) },
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        onPostClick(post.id)
                                    },
                                    onLongClick = {
                                        onMoreClick(post.id)
                                    },
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                )
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        },
    )
}
