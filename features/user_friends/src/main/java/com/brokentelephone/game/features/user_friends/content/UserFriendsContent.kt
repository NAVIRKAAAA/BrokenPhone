package com.brokentelephone.game.features.user_friends.content

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
import com.brokentelephone.game.core.profile.FriendsSectionHeader
import com.brokentelephone.game.core.profile.FriendsShimmerList
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.text_field.SearchTextField
import com.brokentelephone.game.core.text_field.SearchTextFieldHeight
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.features.user_friends.model.UserFriendsState

@Composable
fun UserFriendsContent(
    state: UserFriendsState,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchClear: () -> Unit,
    onUserClick: (String) -> Unit,
    onAddFriendClick: (userId: String) -> Unit,
    onCancelRequestClick: (userId: String) -> Unit,
    onRemoveFriendClick: (userId: String) -> Unit,
    onAcceptRequestClick: (userId: String) -> Unit,
    onSuggestedAddFriendClick: (userId: String) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val user = state.user

    LaunchedEffect(listState.interactionSource) {
        listState.interactionSource.interactions.collect { interaction ->
            if (interaction is DragInteraction.Start) focusManager.clearFocus()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background),
    ) {

        UserFriendsTopBar(
            username = user?.username,
            onBackClick = onBackClick,
        )

        val searchBarTopOffset = SearchTextFieldHeight + 8.dp

        val pullToRefreshState = rememberPullToRefreshState()
        val isRefreshing =
            state.isRefreshing || (state.isLoading && state.filteredFriends.isNotEmpty() && state.suggestedUsers.isNotEmpty())

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
                    isLoading = state.isLoading && state.filteredFriends.isEmpty() && state.suggestedUsers.isEmpty(),
                    shimmerContent = { FriendsShimmerList() },
                    content = {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(
                                top = searchBarTopOffset + 16.dp,
                                bottom = 16.dp
                            ),
                        ) {
                            itemsIndexed(
                                items = state.filteredFriends,
                                key = { _, item -> item.user.id },
                            ) { index, item ->
                                Column(
                                    modifier = Modifier
                                        .animateItem()
                                        .clickable(
                                            onClick = { onUserClick(item.user.id) },
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        )
                                ) {
                                    if (index != 0) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                    AddFriendUserItem(
                                        item = item,
                                        onAddFriendClick = { onAddFriendClick(item.user.id) },
                                        onRemoveFriendClick = { onRemoveFriendClick(item.user.id) },
                                        onAcceptRequestClick = { onAcceptRequestClick(item.user.id) },
                                        onCancelRequestClick = { onCancelRequestClick(item.user.id) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        isLoading = item.user.id in state.addingFriendUserIds,
                                    )
                                    if (index != state.filteredFriends.lastIndex) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        HorizontalDivider(color = MaterialTheme.appColors.divider)
                                    } else {
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }

                            item {
                                FriendsSectionHeader(title = stringResource(R.string.friends_suggested))
                            }

                            itemsIndexed(
                                items = state.suggestedUsers,
                                key = { _, item -> "suggested_${item.user.id}" },
                            ) { index, item ->
                                Column(
                                    modifier = Modifier
                                        .animateItem()
                                        .clickable(
                                            onClick = { onUserClick(item.user.id) },
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        )
                                ) {
                                    if (index != 0) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }

                                    AddFriendUserItem(
                                        item = item,
                                        onAddFriendClick = { onSuggestedAddFriendClick(item.user.id) },
                                        onRemoveFriendClick = {
                                            return@AddFriendUserItem
                                        },
                                        onCancelRequestClick = { onCancelRequestClick(item.user.id) },
                                        onAcceptRequestClick = { onAcceptRequestClick(item.user.id) },
                                        isLoading = item.user.id in state.acceptingUserIds || item.user.id in state.sendingRequestUserIds,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                    )
                                    if (index != state.suggestedUsers.lastIndex) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        HorizontalDivider(color = MaterialTheme.appColors.divider)
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.navigationBarsPadding())
                            }
                        }
                    }
                )
            }

            SearchTextField(
                text = state.searchQuery,
                onTextChange = onSearchQueryChange,
                placeholder = stringResource(R.string.friends_search_placeholder),
                onClearClick = onSearchClear,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onImeAction = {
                    focusManager.clearFocus()
                }
            )
        }

    }
}

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
private fun UserFriendsContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        UserFriendsContent(
            state = UserFriendsState(
                filteredFriends = previewUsers,
                isLoading = false
            ),
            onBackClick = {},
            onRefresh = {},
            onSearchQueryChange = {},
            onSearchClear = {},
            onUserClick = {},
            onAddFriendClick = {},
            onRemoveFriendClick = {},
            onCancelRequestClick = {},
            onAcceptRequestClick = {},
            onSuggestedAddFriendClick = {}
        )
    }
}
