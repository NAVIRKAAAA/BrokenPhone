package com.broken.telephone.features.dashboard.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.data.repository.MockPostRepository
import com.broken.telephone.features.dashboard.model.DashboardState
import com.broken.telephone.features.dashboard.model.toUi
import com.broken.telephone.features.profile.model.UserUi

@Composable
fun DashboardContent(
    state: DashboardState,
    listState: LazyListState,
    onPostClick: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        DashboardTopBar()

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
                            .clickable(onClick = {
                                onPostClick(postUi.id)
                            })
                    ) {
                        if(index != 0) {
                            Spacer(modifier = Modifier.height(16.dp))

                        }

                        PostElement(
                            post = postUi,
                            isUsersPost = postUi.authorId == state.user?.id,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        if(index != state.posts.lastIndex) {

                            Spacer(modifier = Modifier.height(16.dp))

                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
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
    BrokenTelephoneTheme() {
        DashboardContent(
            state = DashboardState(
                posts = MockPostRepository.mockList.map { it.toUi() },
                user = UserUi(
                    id = "user_1",
                    username = "",
                    email = "",
                    avatarUrl = "",
                )
            ),
            onPostClick = {},
            listState = rememberLazyListState()
        )
    }
}