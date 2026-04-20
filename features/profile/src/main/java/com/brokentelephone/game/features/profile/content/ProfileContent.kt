package com.brokentelephone.game.features.profile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.button.BTButton
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.profile.AccountInfoSection
import com.brokentelephone.game.core.profile.ProfilePostsPage
import com.brokentelephone.game.core.profile.ProfileTabRow
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.ProfileTopBar
import com.brokentelephone.game.features.profile.model.ProfileState
import kotlinx.coroutines.launch

@Composable
fun ProfileContent(
    state: ProfileState,
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSignInClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    onTabSelect: (ProfileTab) -> Unit,
    onScrollDirectionChange: (Boolean) -> Unit,
    onPostClick: (postId: String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onRefresh: () -> Unit,
    onFriendsClick: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val pagerState = rememberPagerState(
        initialPage = state.selectedTab.ordinal,
        pageCount = { ProfileTab.entries.size }
    )
    val scope = rememberCoroutineScope()
    val isScrolledPastAccountInfo by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
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

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {

            val pullToRefreshState = rememberPullToRefreshState()

            val isRefreshing =
                (state.isRefreshing || state.isContributionsLoading || state.isPostsLoading) && !state.isInitialLoading

            ProfileTopBar(
                title = stringResource(R.string.profile_title),
                username = state.user?.username.orEmpty(),
                avatarUrl = state.user?.avatarUrl,
                isScrolled = isScrolledPastAccountInfo,
                onTitleClick = { scope.launch { listState.animateScrollToItem(0) } },
                actions = {
                    if (state.isAuth) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = stringResource(R.string.profile_top_bar_edit),
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_settings),
                            contentDescription = stringResource(R.string.profile_top_bar_settings),
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
            )

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
                        AccountInfoSection(
                            username = state.user?.username.orEmpty(),
                            avatarUrl = state.user?.avatarUrl,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp),
                            postsCount = state.myPosts.size,
                            contributions = state.myContributions.size,
                            friends = state.friendsCount,
                            isAuth = state.isAuth,
                            bio = state.user?.bio.orEmpty(),
                            createdAt = state.user?.createdAt,
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
                    }

                    if (state.isAuth) {
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
                                            emptyContent = {
                                                EmptyPostsElement(
                                                    modifier = Modifier.padding(
                                                        horizontal = 16.dp,
                                                        vertical = 16.dp
                                                    ),
                                                )
                                            }
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
                                            emptyContent = {
                                                EmptyContributionsElement(
                                                    modifier = Modifier.padding(
                                                        horizontal = 16.dp,
                                                        vertical = 16.dp
                                                    ),
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }

                    } else {
                        item {
                            Column() {
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    BTButton(
                                        text = stringResource(R.string.profile_sign_in),
                                        onClick = onSignInClick,
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                    )


                                    Spacer(modifier = Modifier.width(12.dp))


                                    BTButton(
                                        text = stringResource(R.string.welcome_get_started),
                                        onClick = onGetStartedClick,
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = stringResource(R.string.profile_guest_note),
                                    textAlign = TextAlign.Center,
                                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun ProfileContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        ProfileContent(
            state = ProfileState(
                user = UserUi(
                    id = "user-1",
                    username = "Alex",
                    avatarUrl = null,
                    email = "",
                    createdAt = System.currentTimeMillis(),
//                    bio = "I love drawing, creative games, and exploring new ideas. Always up for a challenge and meeting new people through fun activities!",
                ),
//                myPosts = MockPostRepository.mockList.map { it.toUi() },
//                myContributions = MockPostRepository.mockList.map { it.toUi() },
                isAuth = true,
                isPostsLoading = false
            ),
            onEditClick = {},
            onSettingsClick = {},
            onSignInClick = {},
            onGetStartedClick = {},
            onTabSelect = {},
            onScrollDirectionChange = {},
            onPostClick = { },
            onMoreClick = {},
            onRefresh = {},
            onFriendsClick = {},
        )
    }
}
