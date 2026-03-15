package com.brokentelephone.game.features.blocked_users.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.features.blocked_users.model.BlockedUserUi
import com.brokentelephone.game.features.blocked_users.model.BlockedUsersState
import com.brokentelephone.game.features.edit_profile.content.EditProfileTopBar

@Composable
fun BlockedUsersContent(
    state: BlockedUsersState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onUnblockClick: (BlockedUserUi) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = stringResource(R.string.blocked_users_title),
            onBackClick = onBackClick,
        )

        val pullToRefreshState = rememberPullToRefreshState()
        val isRefreshing =
            state.isRefreshing || (state.isLoading && state.blockedUsers.isNotEmpty())

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
                isLoading = state.isLoading && !state.isLoadRetrying,
                isEmpty = !state.isLoading && state.blockedUsers.isEmpty() && state.loadError == null,
                shimmerContent = {
                    BlockedUsersShimmerList()
                },
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp),
                    ) {
                        itemsIndexed(
                            items = state.blockedUsers,
                            key = { _, item -> item.id },
                        ) { index, user ->
                            Column {
                                if (index != 0) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                                BlockedUserItem(
                                    blockedUserUi = user,
                                    onUnblockClick = { onUnblockClick(user) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                )
                                if (index != state.blockedUsers.lastIndex) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    HorizontalDivider(color = MaterialTheme.appColors.divider)
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.navigationBarsPadding())
                        }
                    }
                },
                emptyContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f),
                        contentAlignment = Alignment.Center
                    ) {
                        BlockedUsersEmptyContent(modifier = Modifier)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun BlockedUsersContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        BlockedUsersContent(
            state = BlockedUsersState(
                blockedUsers = listOf(
                    BlockedUserUi(
                        id = "block_1",
                        name = "Alice",
                        avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                        createdAt = System.currentTimeMillis() - 60_000 * 5,
                    ),
                    BlockedUserUi(
                        id = "block_2",
                        name = "Bob",
                        avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png",
                        createdAt = System.currentTimeMillis() - 60_000 * 60 * 24,
                    ),
                    BlockedUserUi(
                        id = "block_3",
                        name = "Diana",
                        avatarUrl = null,
                        createdAt = System.currentTimeMillis() - 60_000 * 60 * 24 * 7,
                    ),
                ),
                isLoading = false
            )
        )
    }
}
