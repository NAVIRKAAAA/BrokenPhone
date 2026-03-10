package com.brokentelephone.game.features.dashboard.content

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.data.repository.MockPostRepository
import com.brokentelephone.game.features.dashboard.model.DashboardSort
import com.brokentelephone.game.features.dashboard.model.DashboardState
import com.brokentelephone.game.features.dashboard.model.toUi
import com.brokentelephone.game.features.profile.model.UserUi

@Composable
fun DashboardContent(
    state: DashboardState,
    listState: LazyListState,
    onPostClick: (postId: String) -> Unit,
    onMoreClick: (postId: String) -> Unit,
    onSortSelected: (DashboardSort) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        DashboardTopBar(
            name = state.user?.username ?: "",
            selectedSort = state.selectedSort,
            onSortSelected = onSortSelected,
        )

        if (state.isLoading) {
            DashboardShimmerList()
            return@Column
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {

            itemsIndexed(
                items = state.posts,
                key = { _, item -> item.id }
            ) { index, postUi ->

                Column {
                    Column(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {
                                    onPostClick(postUi.id)
                                },
                                onLongClick = {
                                    onMoreClick(postUi.id)
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    ) {
                        if (index != 0) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        PostElement(
                            post = postUi,
                            isUsersPost = postUi.authorId == state.user?.id,
                            onMoreClick = { onMoreClick(postUi.id) },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (index != state.posts.lastIndex) {

                            HorizontalDivider(color = MaterialTheme.appColors.divider)
                        }
                    }
                }


            }

            item {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }

        }
    }

}

@Preview
@Composable
fun DashboardContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        DashboardContent(
            state = DashboardState(
                posts = MockPostRepository.mockList.map { it.toUi() },
                user = UserUi(
                    id = "user_1",
                    username = "Alex",
                    email = "",
                    avatarUrl = "",
                    createdAt = 0
                ),
                isLoading = true
            ),
            onPostClick = {},
            onMoreClick = {},
            onSortSelected = {},
            listState = rememberLazyListState()
        )
    }
}