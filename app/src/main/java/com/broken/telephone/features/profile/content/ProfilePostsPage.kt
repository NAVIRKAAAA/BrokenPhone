package com.broken.telephone.features.profile.content

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.broken.telephone.core.theme.appColors
import com.broken.telephone.features.dashboard.content.PostElement
import com.broken.telephone.features.dashboard.model.PostUi

@Composable
fun ProfilePostsPage(
    posts: List<PostUi>,
    onScrollDirectionChange: (isScrollingUp: Boolean) -> Unit,
    onPostClick: (postId: String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousScrollOffset = listState.firstVisibleItemScrollOffset
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val isScrollingUp = if (index != previousIndex) {
                    index < previousIndex
                } else {
                    offset <= previousScrollOffset
                }
                previousIndex = index
                previousScrollOffset = offset
                onScrollDirectionChange(isScrollingUp)
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        itemsIndexed(
            items = posts,
            key = { _, item -> item.id },
        ) { index, post ->
            if (index != 0) {
                HorizontalDivider(color = MaterialTheme.appColors.divider)
            }

            PostElement(
                post = post,
                isUsersPost = false,
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

        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
