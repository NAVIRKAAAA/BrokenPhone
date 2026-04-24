package com.brokentelephone.game.features.user_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.button.FriendshipActionButton
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.profile.AccountInfoSection
import com.brokentelephone.game.core.profile.AccountInfoSectionShimmer
import com.brokentelephone.game.core.profile.ProfilePostsPage
import com.brokentelephone.game.core.profile.ProfileTabRow
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.ProfileTopBar
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.features.user_details.model.UserDetailsState
import kotlinx.coroutines.launch

@Composable
fun UserDetailsContent(
    state: UserDetailsState,
    onBackClick: () -> Unit,
    onTabSelect: (ProfileTab) -> Unit,
    onAddFriendClick: () -> Unit,
    onAcceptRequestClick: () -> Unit,
    onCancelRequestClick: () -> Unit,
    onRemoveFriendClick: () -> Unit,
    onPostClick: (postId: String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onFriendsClick: () -> Unit,
    onMoreVertClick: () -> Unit,
    listState: LazyListState = rememberLazyListState(),
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val isScrolledPastAccountInfo by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    val scope = rememberCoroutineScope()

    val user = state.user

    val pagerState = rememberPagerState(
        initialPage = state.selectedTab.ordinal,
        pageCount = { ProfileTab.entries.size }
    )

    val nestedScrollConnection = remember(listState) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val consumed = listState.dispatchRawDelta(-available.y)
                return Offset(0f, -consumed)
            }
        }
    }

    LaunchedEffect(state.selectedTab) {
        pagerState.animateScrollToPage(state.selectedTab.ordinal)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onTabSelect(ProfileTab.entries[page])
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        ProfileTopBar(
            title = stringResource(R.string.profile_title),
            username = user?.username.orEmpty(),
            avatarUrl = user?.avatarUrl,
            isScrolled = isScrolledPastAccountInfo,
            actions = {
                IconButton(
                    onClick = onMoreVertClick,
                    enabled = user != null,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = if (user == null) Modifier.shimmer(cornerRadius = 4.dp) else Modifier,
                    )
                }
            },
            onBackClick = onBackClick,
            onTitleClick = { scope.launch { listState.animateScrollToItem(0) } },
        )

        val pullToRefreshState = rememberPullToRefreshState()
        val isRefreshing = state.isRefreshing

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
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {

                item {

                    ShimmerContent(
                        isLoading = user == null,
                        shimmerContent = {
                            AccountInfoSectionShimmer(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 16.dp),
                            )
                        },
                        content = {
                            if (user != null) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    AccountInfoSection(
                                        username = user.username,
                                        avatarUrl = user.avatarUrl,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .padding(top = 16.dp),
                                        postsCount = state.myPosts.size,
                                        contributions = state.myContributions.size,
                                        friends = state.friendsCount,
                                        bio = user.bio,
                                        createdAt = user.createdAt,
                                        onFriendsClick = onFriendsClick,
                                        onPostsClick = {
                                            onTabSelect(ProfileTab.POSTS)
                                            scope.launch {
                                                listState.animateScrollToItem(1)
                                            }
                                        },
                                        onContributionsClick = {
                                            onTabSelect(ProfileTab.CONTRIBUTIONS)
                                            scope.launch {
                                                listState.animateScrollToItem(1)
                                            }
                                        },
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp)
                                    ) {
                                        FriendshipActionButton(
                                            state = state.friendshipActionState,
                                            onAddFriendClick = onAddFriendClick,
                                            onAcceptRequestClick = onAcceptRequestClick,
                                            onCancelRequestClick = onCancelRequestClick,
                                            onRemoveFriendClick = onRemoveFriendClick,
                                            isLoading = state.isFriendshipActionLoading,
                                            modifier = Modifier.weight(1f),
                                        )

                                        if(state.friendshipActionState != FriendshipActionState.INVITE_RECEIVED) {
                                            Spacer(modifier = Modifier.width(12.dp))

                                            ShareProfileButton(
                                                onClick = {},
                                                isLoading = false
                                            )
                                        }
                                    }
                                }
                            }
                        },
                    )


                }


                stickyHeader {
                    ProfileTabRow(
                        tabs = ProfileTab.entries,
                        selectedIndex = state.selectedTab.ordinal,
                        onTabSelect = onTabSelect,
                    )
                }

                item {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillParentMaxHeight(),
                        verticalAlignment = Alignment.Top,
                        beyondViewportPageCount = 1
                    ) { page ->
                        when (ProfileTab.entries[page]) {
                            ProfileTab.POSTS -> {
                                val showShimmerEffect =
                                    state.isPostsLoading && state.myPosts.isEmpty() && state.isInitialLoading

                                ProfilePostsPage(
                                    posts = state.myPosts,
                                    isLoading = showShimmerEffect,
                                    nestedScrollConnection = nestedScrollConnection,
                                    onPostClick = onPostClick,
                                    onMoreClick = onMoreClick,
                                )
                            }

                            ProfileTab.CONTRIBUTIONS -> {
                                val showShimmerEffect =
                                    state.isContributionsLoading && state.myContributions.isEmpty() && state.isInitialLoading

                                ProfilePostsPage(
                                    posts = state.myContributions,
                                    isLoading = showShimmerEffect,
                                    nestedScrollConnection = nestedScrollConnection,
                                    onPostClick = onPostClick,
                                    onMoreClick = onMoreClick,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview()
@Composable
private fun UserDetailsContentPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        UserDetailsContent(
            onAddFriendClick = {},
            onAcceptRequestClick = {},
            onCancelRequestClick = {},
            onRemoveFriendClick = {},
            onFriendsClick = {},
            onPostClick = {},
            onMoreClick = {},
            onMoreVertClick = {},
            state = UserDetailsState(
                friendshipActionState = FriendshipActionState.INVITE_RECEIVED,
                user = UserUi(
                    id = "user-1",
                    username = "Alex",
                    avatarUrl = null,
                    email = "",
                    createdAt = System.currentTimeMillis(),
                    bio = "I love drawing, creative games, and exploring new ideas. Always up for a challenge and meeting new people through fun activities!",
                ),
            ),
            onBackClick = {},
            onTabSelect = {}
        )
    }
}
