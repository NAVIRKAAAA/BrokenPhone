package com.brokentelephone.game.features.chain_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.button.FloatingActionButton
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.features.chain_details.model.ChainDetailsState
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChainDetailsContent(
    state: ChainDetailsState,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onUserClick: (userId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    val chainSize = state.chain.size - 1
    val maxGenerations = state.post?.maxGenerations ?: 0

    // status bars + top padding + icon + spacer
    val listContentTopPadding =
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 80.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        FloatingActionButton(
            imageResId = R.drawable.ic_back,
            onClick = onBackClick,
            modifier = Modifier
                .zIndex(1f)
                .statusBarsPadding()
                .padding(8.dp)
        )

        ShimmerContent(
            isLoading = state.chain.isEmpty(),
            shimmerContent = {
                ChainDetailsShimmerList(modifier = Modifier.padding(horizontal = 16.dp))
            },
            content = {
                val pullToRefreshState = rememberPullToRefreshState()

                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = onRefresh,
                    state = pullToRefreshState,
                    modifier = Modifier.fillMaxSize(),
                    indicator = {
                        AppPullToRefreshIndicator(
                            state = pullToRefreshState,
                            isRefreshing = state.isRefreshing,
                            modifier = Modifier.align(Alignment.TopCenter),
                        )
                    },
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        state = lazyListState,
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(top = listContentTopPadding, bottom = 16.dp),
                    ) {

                        itemsIndexed(
                            items = state.chain,
                            key = { _, item -> item.id }
                        ) { index, postUi ->
                            val isLast = index == state.chain.lastIndex
                            val isHidden =
                                chainSize != maxGenerations && (postUi.authorId != state.userId || postUi.id != state.postId)

                            if (!isHidden) {
                                ChainDetailsElement(
                                    post = postUi,
                                    onUserClick = { onUserClick(postUi.authorId) }
                                )
                            } else {
                                val hiddenContent = when (postUi.content) {
                                    is PostContent.Text -> PostContent.Text("")
                                    is PostContent.Drawing -> PostContent.Drawing("")
                                }
                                ChainDetailsElementHidden(content = hiddenContent)
                            }

                            if (!isLast) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(24.dp),
                                    contentAlignment = Alignment.CenterStart,
                                ) {
                                    VerticalDivider(
                                        thickness = 2.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(start = 32.dp),
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))

                            HorizontalDivider(color = MaterialTheme.appColors.divider)

                            Spacer(modifier = Modifier.height(24.dp))
                            if (chainSize == maxGenerations) {
                                Row(
                                    modifier = modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        6.dp,
                                        Alignment.CenterHorizontally
                                    )
                                ) {

                                    Icon(
                                        painter = painterResource(R.drawable.ic_complete),
                                        contentDescription = null,
                                        tint = MaterialTheme.appColors.badgeComplete,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Text(
                                        text = stringResource(R.string.chain_details_complete),
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        color = MaterialTheme.appColors.badgeComplete
                                    )

                                }

                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Text(
                                text = stringResource(
                                    R.string.chain_details_generations,
                                    chainSize,
                                    maxGenerations
                                ),
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(32.dp)
                                    .navigationBarsPadding()
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun ChainDetailsContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        ChainDetailsContent(
            state = ChainDetailsState(
                isLoading = false,
                postId = "1",
                chain = listOf(
                    PostUi(
                        id = "1", authorId = "user_1", authorName = "Alex",
                        avatarUrl = null,
                        content = PostContent.Text("Once upon a time there was a broken telephone that nobody could fix..."),
                        createdAt = System.currentTimeMillis() - 3_600_000,
                        generation = 1, maxGenerations = 5,
                        status = PostStatus.AVAILABLE, nextTimeLimit = 60,
                    ),
                    PostUi(
                        id = "2", authorId = "user_2", authorName = "Maria",
                        avatarUrl = null,
                        content = PostContent.Drawing(),
                        createdAt = System.currentTimeMillis() - 2_700_000,
                        generation = 2, maxGenerations = 5,
                        status = PostStatus.AVAILABLE, nextTimeLimit = 45,
                    ),
                    PostUi(
                        id = "3", authorId = "user_1", authorName = "Alex",
                        avatarUrl = null,
                        content = PostContent.Text("A cat was sitting on a telephone wire singing songs"),
                        createdAt = System.currentTimeMillis() - 1_800_000,
                        generation = 3, maxGenerations = 5,
                        status = PostStatus.AVAILABLE, nextTimeLimit = 60,
                    ),
                    PostUi(
                        id = "4", authorId = "user_3", authorName = "John",
                        avatarUrl = null,
                        content = PostContent.Drawing(),
                        createdAt = System.currentTimeMillis() - 900_000,
                        generation = 4, maxGenerations = 5,
                        status = PostStatus.AVAILABLE, nextTimeLimit = 30,
                    ),
                    PostUi(
                        id = "5", authorId = "user_4", authorName = "Sofia",
                        avatarUrl = null,
                        content = PostContent.Text("The telephone sang a broken cat on the wire"),
                        createdAt = System.currentTimeMillis() - 300_000,
                        generation = 5, maxGenerations = 5,
                        status = PostStatus.AVAILABLE, nextTimeLimit = 60,
                    ),
                ),
                userId = "user_1",
                post = PostUi(
                    id = "1",
                    authorId = "user_1",
                    authorName = "Alex",
                    avatarUrl = null,
//                    content = PostContent.Drawing(),
                    content = PostContent.Text("Once upon a time there was a broken telephone..."),
                    createdAt = System.currentTimeMillis() - 300000,
                    generation = 5,
                    maxGenerations = 5,
                    status = PostStatus.AVAILABLE,
                    nextTimeLimit = 60,
                )
            ),
            onBackClick = {},
            onRefresh = {},
            onUserClick = {},
        )
    }
}
