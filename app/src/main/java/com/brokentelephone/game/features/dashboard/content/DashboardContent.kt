package com.brokentelephone.game.features.dashboard.content

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.banner.ActiveSessionBanner
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.pagination.LoadMoreIndicator
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.domain.model.sort.DashboardSort
import com.brokentelephone.game.features.dashboard.model.DashboardState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    state: DashboardState,
    listState: LazyListState,
    onPostClick: (postId: String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onUserClick: (userId: String) -> Unit,
    onSortSelected: (DashboardSort) -> Unit,
    onTitleClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DashboardTopBar(
            name = state.user?.username ?: "",
            selectedSort = state.selectedSort,
            onSortSelected = onSortSelected,
            onTitleClick = onTitleClick,
            onNotificationsClick = onNotificationsClick,
        )

        val pullToRefreshState = rememberPullToRefreshState()
        val isRefreshing =
            state.isRefreshing || (state.isInitialLoading && state.posts.isNotEmpty())

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize(),
            indicator = {
                AppPullToRefreshIndicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            },
        ) {
            ShimmerContent(
                isLoading = state.posts.isEmpty(),
                shimmerContent = {
                    DashboardShimmerList()
                },
                content = {
                    val reachedEnd = remember {
                        derivedStateOf {
                            val layoutInfo = listState.layoutInfo
                            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                                ?: return@derivedStateOf false
                            lastVisible >= layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
                        }
                    }
                    LaunchedEffect(reachedEnd.value) {
                        if (reachedEnd.value) onLoadMore()
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp),
                    ) {
                        itemsIndexed(
                            items = state.posts,
                            key = { _, item -> item.id }
                        ) { index, postUi ->
                            Column(
                                modifier = Modifier
                                    .animateItem()
                                    .combinedClickable(
                                        onClick = { onPostClick(postUi.id) },
                                        onLongClick = { onMoreClick(postUi.id) },
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    )
                            ) {
                                if (index != 0) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                                PostElement(
                                    post = postUi,
                                    isUsersPost = postUi.authorId == state.user?.id,
                                    onMoreClick = { onMoreClick(postUi.id) },
                                    onUserClick = { onUserClick(postUi.authorId) },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                if (index != state.posts.lastIndex) {
                                    HorizontalDivider(color = MaterialTheme.appColors.divider)
                                }
                            }
                        }
                        if (state.hasMore) {
                            item { LoadMoreIndicator() }
                        }
                        item {
                            Spacer(modifier = Modifier.navigationBarsPadding())
                        }
                    }
                }
            )
        }
    }
}

const val LOAD_MORE_THRESHOLD = 3

@Preview
@Composable
fun DashboardContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DashboardContent(
                state = DashboardState(
                    user = UserUi(
                        id = "user_1",
                        username = "Alex",
                        email = "",
                        avatarUrl = "",
                        createdAt = 0
                    ),
                    isInitialLoading = true
                ),
                onPostClick = {},
                onMoreClick = {},
                onUserClick = {},
                onSortSelected = {},
                onTitleClick = {},
                onNotificationsClick = {},
                onRefresh = {},
                onLoadMore = {},
                listState = rememberLazyListState(),
            )

            ActiveSessionBanner(
                visible = true,
                formattedTime = "00:29",
                progress = 0.65f,
                onContinueClick = {},
                onDismiss = {},
                isLoading = false,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}
