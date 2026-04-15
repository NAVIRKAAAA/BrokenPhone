package com.brokentelephone.game.features.add_friend.content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.model.user.AddFriendUserUi
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.profile.AddFriendUserItem
import com.brokentelephone.game.core.profile.FriendRequestItem
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.text_field.SearchTextField
import com.brokentelephone.game.core.text_field.SearchTextFieldHeight
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.core.top_bar.EditProfileTopBar
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.features.add_friend.model.AddFriendState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddFriendContent(
    state: AddFriendState,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchClear: () -> Unit,
    onUserClick: (userId: String) -> Unit,
    onAddFriendClick: (userId: String) -> Unit,
    onCancelRequestClick: (userId: String) -> Unit,
    onRemoveFriendClick: (userId: String) -> Unit,
    onAcceptRequestClick: (userId: String) -> Unit,
    onDeclineRequestClick: (userId: String) -> Unit,
    onRefresh: () -> Unit,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
) {
    val searchBarTopOffset = SearchTextFieldHeight + 8.dp
    val focusManager = LocalFocusManager.current

    LaunchedEffect(listState.interactionSource) {
        listState.interactionSource.interactions.collect { interaction ->
            if (interaction is DragInteraction.Start) focusManager.clearFocus()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = stringResource(R.string.add_friend_title),
            onBackClick = onBackClick,
        )

        val pullToRefreshState = rememberPullToRefreshState()
        val isRefreshing =
            state.isRefreshing || (state.isLoading && state.pendingInvites.isNotEmpty()) || state.isSearching

        Box(modifier = Modifier.fillMaxSize()) {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                state = pullToRefreshState,
                modifier = Modifier.fillMaxSize(),
                indicator = {
                    AppPullToRefreshIndicator(
                        state = pullToRefreshState,
                        isRefreshing = isRefreshing,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = searchBarTopOffset),
                    )
                },
            ) {
                ShimmerContent(
                    isLoading = state.isLoading && state.pendingInvites.isEmpty(),
                    shimmerContent = { AddFriendShimmerList() },
                    content = {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                top = searchBarTopOffset + 16.dp,
                                bottom = 16.dp,
                            ),
                            modifier = Modifier
                                .fillMaxSize(),
                            state = listState
                        ) {
                            itemsIndexed(
                                items = state.results,
                                key = { _, item -> item.user.id },
                            ) { index, item ->
                                Column(
                                    modifier = Modifier
                                        .animateItem()
                                        .clickable(
                                            onClick = { onUserClick(item.user.id) },
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                        )
                                ) {
                                    if (index != 0) Spacer(modifier = Modifier.height(16.dp))
                                    AddFriendUserItem(
                                        item = item,
                                        onAddFriendClick = { onAddFriendClick(item.user.id) },
                                        onCancelRequestClick = { onCancelRequestClick(item.user.id) },
                                        onRemoveFriendClick = { onRemoveFriendClick(item.user.id) },
                                        isLoading = item.user.id in state.addingFriendUserIds,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                    )
                                    if (index != state.results.lastIndex) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        HorizontalDivider(color = MaterialTheme.appColors.divider)
                                    } else {
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }

                            item(key = "received_header") {
                                AddFriendPendingHeader(
                                    count = state.receivedPendingInvites.size,
                                    title = stringResource(R.string.add_friend_received_invites),
                                )
                            }

                            itemsIndexed(
                                items = state.receivedPendingInvites,
                                key = { _, item -> "received_${item.user.id}" },
                            ) { index, item ->
                                Column(
                                    modifier = Modifier
                                        .animateItem()
                                        .clickable(
                                            onClick = { onUserClick(item.user.id) },
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                        )
                                ) {
                                    if (index != 0) Spacer(modifier = Modifier.height(16.dp))
                                    FriendRequestItem(
                                        user = item.user,
                                        onAcceptClick = { onAcceptRequestClick(item.user.id) },
                                        onDeclineClick = { onDeclineRequestClick(item.user.id) },
                                        isAcceptLoading = item.user.id in state.acceptingUserIds,
                                        isDeclineLoading = item.user.id in state.decliningUserIds,
                                        enabled = item.user.id !in state.acceptingUserIds && item.user.id !in state.decliningUserIds,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                    )
                                    if (index != state.receivedPendingInvites.lastIndex) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        HorizontalDivider(color = MaterialTheme.appColors.divider)
                                    } else {
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }

                            item(key = "pending_header") {
                                AddFriendPendingHeader(count = state.pendingInvites.size)
                            }

                            itemsIndexed(
                                items = state.pendingInvites,
                                key = { _, item -> "pending_${item.user.id}" },
                            ) { index, item ->
                                Column(
                                    modifier = Modifier
                                        .animateItem()
                                        .clickable(
                                            onClick = { onUserClick(item.user.id) },
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() },
                                        )
                                ) {
                                    if (index != 0) Spacer(modifier = Modifier.height(16.dp))
                                    AddFriendUserItem(
                                        item = item,
                                        onAddFriendClick = {},
                                        onCancelRequestClick = { onCancelRequestClick(item.user.id) },
                                        onRemoveFriendClick = { onRemoveFriendClick(item.user.id) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                    )
                                    if (index != state.pendingInvites.lastIndex) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        HorizontalDivider(color = MaterialTheme.appColors.divider)
                                    }
                                }
                            }

                            item { Spacer(modifier = Modifier.navigationBarsPadding()) }
                        }
                    }
                )
            }

            SearchTextField(
                text = state.searchQuery,
                onTextChange = onSearchQueryChange,
                placeholder = stringResource(R.string.add_friend_search_placeholder),
                onClearClick = onSearchClear,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onImeAction = {
                    focusManager.clearFocus()
                },
            )
        }
    }
}

private val previewPendingInvites = listOf(
    AddFriendUserUi(
        user = UserUi(
            id = "p1",
            username = "bob_pending",
            email = "",
            avatarUrl = null,
            createdAt = 1740300000000L,
//            authProvider = AuthProvider.EMAIL
        ),
        friendshipState = FriendshipActionState.INVITE_SENT,
    ),
    AddFriendUserUi(
        user = UserUi(
            id = "p2",
            username = "carol_waiting",
            email = "",
            avatarUrl = null,
            createdAt = 1740400000000L,
//            authProvider = AuthProvider.EMAIL
        ),
        friendshipState = FriendshipActionState.INVITE_SENT,
    ),
)

private val previewUsers = listOf(
    AddFriendUserUi(
        user = UserUi(
            id = "1",
            username = "alex_doe",
            email = "",
            avatarUrl = null,
            createdAt = 1740000000000L,
//            authProvider = AuthProvider.EMAIL
        ),
        friendshipState = FriendshipActionState.NOT_FRIENDS,
    ),
    AddFriendUserUi(
        user = UserUi(
            id = "2",
            username = "alexander_great",
            email = "",
            avatarUrl = null,
            createdAt = 1740100000000L,
//            authProvider = AuthProvider.EMAIL
        ),
        friendshipState = FriendshipActionState.INVITE_SENT,
    ),
    AddFriendUserUi(
        user = UserUi(
            id = "3",
            username = "alexa_smith",
            email = "",
            avatarUrl = null,
            createdAt = 1740200000000L,
//            authProvider = AuthProvider.EMAIL
        ),
        friendshipState = FriendshipActionState.FRIENDS,
    ),
)

@Preview(showBackground = true)
@Composable
private fun AddFriendContentPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        AddFriendContent(
            state = AddFriendState(
                searchQuery = "alex",
                results = previewUsers,
                pendingInvites = previewPendingInvites,
                receivedPendingInvites = previewPendingInvites,
                isLoading = false
            ),
            onBackClick = {},
            onSearchQueryChange = {},
            onSearchClear = {},
            onAddFriendClick = {},
            onCancelRequestClick = {},
            onRemoveFriendClick = {},
            onAcceptRequestClick = {},
            onDeclineRequestClick = {},
            onRefresh = {},
            onUserClick = {}
        )
    }
}
