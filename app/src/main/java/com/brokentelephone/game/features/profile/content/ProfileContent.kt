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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.features.profile.model.ProfileState
import com.brokentelephone.game.features.profile.model.ProfileTab
import com.brokentelephone.game.features.profile.model.UserUi
import com.brokentelephone.game.features.welcome.content.WelcomeButton
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
            val isContributionsRefreshing =
                (state.isContributionsLoading && state.myContributions.isNotEmpty())
            val isPostsRefreshing =
                (state.isPostsLoading && state.myPosts.isNotEmpty())

            val isRefreshing = state.isRefreshing || isContributionsRefreshing || isPostsRefreshing

            ProfileTopBar(
                title = stringResource(R.string.profile_title),
                username = state.user?.username.orEmpty(),
                avatarUrl = state.user?.avatarUrl,
                onEditClick = onEditClick,
                onSettingsClick = onSettingsClick,
                isScrolled = isScrolledPastAccountInfo,
                showEditButton = state.isAuth,
                onTitleClick = { scope.launch { listState.animateScrollToItem(0) } },
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
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                            postsCount = state.myPosts.size,
                            contributions = state.myContributions.size,
                            isAuth = state.isAuth
                        )
                    }

                    if (state.isAuth) {
                        stickyHeader {
                            PrimaryTabRow(
                                selectedTabIndex = state.selectedTab.ordinal,
                                containerColor = MaterialTheme.colorScheme.background,
                                divider = {
                                    HorizontalDivider(color = MaterialTheme.appColors.divider)
                                }
                            ) {
                                ProfileTab.entries.forEachIndexed { index, tab ->
                                    val isSelected = state.selectedTab.ordinal == index
                                    val color = if (isSelected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }

                                    Tab(
                                        selected = isSelected,
                                        onClick = { onTabSelect(tab) },
                                        text = {
                                            Text(
                                                text = stringResource(tab.labelResId),
                                                textAlign = TextAlign.Start,
                                                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                                                fontSize = 17.sp,
                                                lineHeight = 25.sp,
                                                color = color
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillParentMaxHeight(),
                                verticalAlignment = Alignment.Top,
                                beyondViewportPageCount = 1
                            ) { page ->
                                val profileTab = ProfileTab.entries[page]

                                when (profileTab) {
                                    ProfileTab.POSTS -> {
                                        val showShimmerEffect =
                                            state.isPostsLoading && state.myPosts.isEmpty()

                                        ProfilePostsPage(
                                            posts = state.myPosts,
                                            profileTab = ProfileTab.POSTS,
                                            isLoading = showShimmerEffect,
                                            nestedScrollConnection = nestedScrollConnection,
                                            onPostClick = onPostClick,
                                            onMoreClick = onMoreClick,
                                        )
                                    }

                                    ProfileTab.CONTRIBUTIONS -> {
                                        val showShimmerEffect =
                                            state.isContributionsLoading && state.myContributions.isEmpty()

                                        ProfilePostsPage(
                                            posts = state.myContributions,
                                            profileTab = ProfileTab.CONTRIBUTIONS,
                                            isLoading = showShimmerEffect,
                                            nestedScrollConnection = nestedScrollConnection,
                                            onPostClick = onPostClick,
                                            onMoreClick = onMoreClick,
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
                                    WelcomeButton(
                                        text = stringResource(R.string.profile_sign_in),
                                        onClick = onSignInClick,
                                        contentColor = MaterialTheme.colorScheme.onSurface,
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                    )


                                    Spacer(modifier = Modifier.width(12.dp))


                                    WelcomeButton(
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
                    createdAt = 0
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
            onRefresh = {}
        )
    }
}
