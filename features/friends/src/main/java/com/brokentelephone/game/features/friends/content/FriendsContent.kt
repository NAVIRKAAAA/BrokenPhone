package com.brokentelephone.game.features.friends.content

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.profile.AddFriendUserItem
import com.brokentelephone.game.core.profile.FriendItem
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.text_field.SearchTextField
import com.brokentelephone.game.core.text_field.SearchTextFieldHeight
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.core.top_bar.EditProfileTopBar
import com.brokentelephone.game.features.friends.model.FriendsState

@Composable
fun FriendsContent(
    state: FriendsState,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchClear: () -> Unit,
    onRefresh: () -> Unit,
    onUserClick: (String) -> Unit,
    onRemoveFriendClick: (String) -> Unit,
    onAddFriendClick: () -> Unit,
    onSuggestedAddFriendClick: (String) -> Unit,
    onAcceptRequestClick: (String) -> Unit,
    onCancelRequestClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
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
            title = stringResource(R.string.friends_title),
            onBackClick = onBackClick,
            actions = {
                IconButton(onClick = onAddFriendClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_person_add),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
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
                    isLoading = state.isLoading && (state.filteredFriends.isEmpty() && state.suggestedUsers.isEmpty()),
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
                                key = { _, item -> item.id },
                            ) { index, user ->
                                Column(
                                    modifier = Modifier
                                        .animateItem()
                                        .clickable(
                                            onClick = { onUserClick(user.id) },
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        )
                                ) {
                                    if (index != 0) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                    FriendItem(
                                        user = user,
                                        onRemoveClick = { onRemoveFriendClick(user.id) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
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

@Preview(showBackground = true)
@Composable
private fun FriendsContentPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        FriendsContent(
            state = FriendsState(),
            onBackClick = {},
            onSearchQueryChange = {},
            onSearchClear = {},
            onRefresh = {},
            onUserClick = {},
            onRemoveFriendClick = {},
            onAddFriendClick = {},
            onSuggestedAddFriendClick = {},
            onAcceptRequestClick = {},
            onCancelRequestClick = {},
        )
    }
}
