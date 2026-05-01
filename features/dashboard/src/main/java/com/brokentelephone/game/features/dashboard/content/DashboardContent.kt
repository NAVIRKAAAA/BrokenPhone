package com.brokentelephone.game.features.dashboard.content

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.brokentelephone.game.core.composable.pagination.LoadMoreIndicator
import com.brokentelephone.game.core.composable.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.composable.shimmer.ShimmerContent
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.features.dashboard.model.DashboardState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    state: DashboardState,
    listState: LazyListState,
    onPostClick: (postId: String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onUserClick: (userId: String) -> Unit,
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
        val isScrolled by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
            }
        }

        DashboardTopBar(
            name = state.user?.username ?: "",
            isScrolled = isScrolled,
            onTitleClick = onTitleClick,
            onNotificationsClick = onNotificationsClick,
            unreadNotificationsCount = state.unreadNotificationsCount,
            modifier = Modifier.zIndex(1f),
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
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.posts,
                            key = { it.id }
                        ) { postUi ->
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
                                PostElement(
                                    post = postUi,
                                    onMoreClick = { onMoreClick(postUi.id) },
                                    onUserClick = { onUserClick(postUi.authorId) }
                                )
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
                    isInitialLoading = false,
                    unreadNotificationsCount = 4,
                    posts = listOf(
                        PostUi(
                            id = "1",
                            authorId = "user_1",
                            authorName = "Alex",
                            avatarUrl = null,
                            content = PostContent.Text("Once upon a time there was a broken telephone that nobody could fix..."),
                            createdAt = System.currentTimeMillis() - 120_000,
                            generation = 10,
                            maxGenerations = 10,
                            status = PostStatus.AVAILABLE,
                            drawingTimeLimit = 60,
                            textTimeLimit = 60
                        ),
                        PostUi(
                            id = "2",
                            authorId = "user_2",
                            authorName = "Maria",
                            avatarUrl = null,
                            content = PostContent.Drawing(),
                            createdAt = System.currentTimeMillis() - 3_600_000,
                            generation = 3,
                            maxGenerations = 10,
                            status = PostStatus.AVAILABLE,
                            drawingTimeLimit = 60,
                            textTimeLimit = 60
                        ),
                        PostUi(
                            id = "3",
                            authorId = "user_3",
                            authorName = "John".repeat(43),
                            avatarUrl = null,
                            content = PostContent.Text("A mysterious figure appeared at the edge of the forest."),
                            createdAt = System.currentTimeMillis() - 7_200_000,
                            generation = 7,
                            maxGenerations = 10,
                            status = PostStatus.AVAILABLE,
                            drawingTimeLimit = 60,
                            textTimeLimit = 60
                        ),
                    )
                ),
                onPostClick = {},
                onMoreClick = {},
                onUserClick = {},
                onTitleClick = {},
                onNotificationsClick = {},
                onRefresh = {},
                onLoadMore = {},
                listState = rememberLazyListState(),
            )

//            ActiveSessionBanner(
//                visible = true,
//                formattedTime = "00:29",
//                progress = 0.65f,
//                onContinueClick = {},
//                onDismiss = {},
//                isLoading = false,
//                modifier = Modifier.align(Alignment.TopCenter),
//            )
        }
    }
}
