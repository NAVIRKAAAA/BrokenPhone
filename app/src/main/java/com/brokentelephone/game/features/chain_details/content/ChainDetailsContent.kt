package com.brokentelephone.game.features.chain_details.content

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.pull_to_refresh.AppPullToRefreshIndicator
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.features.chain_details.model.ChainDetailsState
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.edit_profile.content.EditProfileTopBar
import com.brokentelephone.game.features.profile.model.UserUi
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChainDetailsContent(
    state: ChainDetailsState,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    val chainSize = state.chain.size
    val maxGenerations = state.post?.maxGenerations ?: 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = stringResource(R.string.chain_details_title),
            onBackClick = onBackClick,
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
                        contentPadding = PaddingValues(vertical = 16.dp),
                    ) {

                        itemsIndexed(
                            items = state.chain,
                            key = { _, item -> item.id }
                        ) { index, postUi ->
                            Log.d(
                                "LOG_TAG",
                                "CLicked post id: ${state.postId}. Current post id: ${postUi.id}"
                            )
                            Column {

                                if (index != 0) {
                                    Spacer(modifier = Modifier.height(24.dp))
                                }

                                ChainDetailsElement(
                                    post = postUi,
                                    isHidden = chainSize != maxGenerations && (postUi.authorId != state.userUi?.id || postUi.id != state.postId)
                                )

                                if (index < maxGenerations) {

                                    Spacer(modifier = Modifier.height(24.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 52.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {

                                        Icon(
                                            painter = painterResource(R.drawable.ic_arrow_down),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                        )


//                            Spacer(modifier = Modifier.width(4.dp))
//
//                            Text(
//                                text = (index + 1).toString(),
//                                textAlign = TextAlign.Center,
//                                fontFamily = FontFamily(Font(R.font.inter_medium)),
//                                fontSize = 16.sp,
//                                lineHeight = 24.sp,
//                                color = MaterialTheme.colorScheme.primary,
//                            )
                                    }

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
        darkTheme = true
    ) {
        val userId = "user_id"
        ChainDetailsContent(
            state = ChainDetailsState(
                isLoading = true,
//                postParentId = "1",
//                chain = MockPostRepository.chainsMockList.mapIndexed { index, entry ->
//                    entry.toUi(
//                        id = Uuid.random().toString(),
//                        generation = index,
//                        maxGenerations = MockPostRepository.chainsMockList.size,
//                        textTimeLimit = 0,
//                        drawingTimeLimit = 0
//                    ).copy(
//                        authorId = if (index == 1) userId else index.toString()
//                    )
//                }.subList(0, 1),
                userUi = UserUi(
                    id = "1",
                    username = "Alex",
                    email = "alex@example.com",
                    avatarUrl = null,
                    authProvider = AuthProvider.EMAIL,
                    createdAt = 1_700_000_000_000L,
                ),
                post = PostUi(
                    id = "1",
                    authorId = "user-1",
                    authorName = "Alex",
                    avatarUrl = null,
//                    content = PostContent.Drawing(),
                    content = PostContent.Text("Once upon a time there was a broken telephone..."),
                    createdAt = System.currentTimeMillis() - 300000,
                    generation = 10,
                    maxGenerations = 10,
                    status = PostStatus.AVAILABLE,
                    nextTimeLimit = 60,
                )
            ),
            onBackClick = {},
            onRefresh = {},
        )
    }
}
