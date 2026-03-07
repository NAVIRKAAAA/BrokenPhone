package com.broken.telephone.features.blocked_users.content

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.core.theme.appColors
import com.broken.telephone.features.blocked_users.model.BlockedUserUi
import com.broken.telephone.features.blocked_users.model.BlockedUsersState
import com.broken.telephone.features.edit_profile.content.EditProfileTopBar

@Composable
fun BlockedUsersContent(
    state: BlockedUsersState,
    modifier: Modifier = Modifier,
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

        if (state.blockedUsers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                BlockedUsersEmptyContent(modifier = Modifier)
            }
        } else {
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
                )
            )
        )
    }
}
