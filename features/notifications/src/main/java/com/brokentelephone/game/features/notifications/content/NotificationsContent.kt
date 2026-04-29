package com.brokentelephone.game.features.notifications.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.layout.ScrollableCenteredContent
import com.brokentelephone.game.core.model.notification.NotificationUi
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.model.notification.NotificationFilter
import com.brokentelephone.game.features.notifications.model.NotificationsState
import com.brokentelephone.game.features.notifications.model.groupByDate

@Composable
fun NotificationsContent(
    state: NotificationsState,
    onBackClick: () -> Unit,
    onFilterSelected: (NotificationFilter) -> Unit,
    onNotificationClick: (NotificationUi) -> Unit,
    onAcceptFriendClick: (NotificationUi.Friends) -> Unit,
    onDeclineFriendClick: (NotificationUi.Friends) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        NotificationsTopBar(
            title = stringResource(R.string.notifications_title),
            onCloseClick = onBackClick,
        )

        val pullToRefreshState = rememberPullToRefreshState()
        val isEmpty = state.groupedNotifications.isEmpty()
        val isRefreshing =
            state.isRefreshing || (state.isLoading && !isEmpty) || state.isLoadingByFilter

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
            Column(modifier = Modifier.fillMaxSize()) {

                NotificationsFilterRow(
                    selectedFilter = state.selectedFilter,
                    onFilterSelected = onFilterSelected,
                )

                val emptyTitle = stringResource(state.selectedFilter.emptyTitleResId)
                val emptyBody = stringResource(state.selectedFilter.emptyBodyResId)

                Box(modifier = Modifier.weight(1f)) {
                    ShimmerContent(
                        isLoading = state.isLoading && isEmpty,
                        isEmpty = !state.isLoading && isEmpty,
                        emptyContent = {
                            ScrollableCenteredContent {
                                NotificationsEmptyState(
                                    title = emptyTitle,
                                    body = emptyBody,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                )
                            }
                        },
                        shimmerContent = { NotificationsShimmerList() },
                        content = {
                            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                                state.groupedNotifications.forEach { (group, notifications) ->
                                    item(key = group.name) {
                                        NotificationsDateHeader(group = group)
                                    }
                                    items(
                                        items = notifications,
                                        key = { it.id },
                                    ) { notification ->
                                        when (notification) {
                                            is NotificationUi.Friends -> FriendNotificationItem(
                                                item = notification,
                                                onClick = { onNotificationClick(notification) },
                                                onAcceptClick = { onAcceptFriendClick(notification) },
                                                onDeclineClick = { onDeclineFriendClick(notification) },
                                                modifier = Modifier.animateItem(),
                                            )

                                            is NotificationUi.ChainInfo -> ChainNotificationItem(
                                                item = notification,
                                                onClick = { onNotificationClick(notification) },
                                                modifier = Modifier.animateItem(),
                                            )

                                            is NotificationUi.News -> NewsNotificationItem(
                                                item = notification,
                                                onClick = { onNotificationClick(notification) },
                                                modifier = Modifier.animateItem(),
                                            )
                                        }
                                    }
                                }

                                item { Spacer(modifier = Modifier.navigationBarsPadding()) }
                            }
                        },
                    )
                } // Box weight(1f)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsContentPreview() {
    val notifications = listOf<NotificationUi>(
//        NotificationUi.Friends(
//            id = "1",
//            createdAt = System.currentTimeMillis() - 5 * 60 * 1000L,
//            isRead = false,
//            requestId = "req_1",
//            userId = "u1",
//            username = "alex_doe",
//            userAvatarUrl = null,
//            type = NotificationData.FriendsType.INVITE_RECEIVED,
//        ),
//        NotificationUi.ChainInfo(
//            id = "2",
//            createdAt = System.currentTimeMillis() - 3 * 60 * 60 * 1000L,
//            isRead = true,
//            chainId = "c1",
//            postId = "p1",
//            title = "Your chain is complete!",
//            body = "Someone finished the chain you started.",
//        ),
//        NotificationUi.News(
//            id = "3",
//            createdAt = System.currentTimeMillis() - 24 * 60 * 60 * 1000L,
//            isRead = false,
//            title = "New feature dropped",
//            body = "You can now use 3 new brush types.",
//        ),
    )
    BrokenTelephoneTheme(darkTheme = false) {
        NotificationsContent(
            state = NotificationsState(
                groupedNotifications = notifications.groupByDate(),
                selectedFilter = NotificationFilter.CHAIN,
                isLoading = false,
            ),
            onBackClick = {},
            onFilterSelected = {},
            onNotificationClick = {},
            onAcceptFriendClick = {},
            onDeclineFriendClick = {},
            onRefresh = {},
        )
    }
}
