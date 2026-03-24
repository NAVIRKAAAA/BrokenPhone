package com.brokentelephone.game.features.profile.content

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.features.dashboard.model.PostUi

@Composable
fun ProfilePostsPage(
    posts: List<PostUi>,
    profileTab: ProfileTab,
    isLoading: Boolean,
    nestedScrollConnection: NestedScrollConnection,
    onPostClick: (postId: String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    ShimmerContent(
        isLoading = isLoading,
        shimmerContent = {
            ProfilePageShimmerList(modifier = modifier)
        },
        isEmpty = !isLoading && posts.isEmpty(),
        emptyContent = {
            if (profileTab == ProfileTab.POSTS) {
                EmptyPostsElement(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                )
            } else {
                EmptyContributionsElement(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                )
            }
        },
        content = {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection),
            ) {
                itemsIndexed(
                    items = posts,
                    key = { _, item -> item.id },
                ) { index, post ->
                    Column(
                        modifier = Modifier.animateItem()
                    ) {
                        if (index != 0) {
                            HorizontalDivider(color = MaterialTheme.appColors.divider)
                        }

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
                                .padding(horizontal = 16.dp, vertical = 16.dp),
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
