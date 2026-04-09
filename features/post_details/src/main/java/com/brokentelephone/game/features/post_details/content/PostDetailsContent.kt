package com.brokentelephone.game.features.post_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.shimmer.ShimmerContent
import com.brokentelephone.game.core.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.features.post_details.model.PostDetailsButtonType
import com.brokentelephone.game.features.post_details.model.PostDetailsState

@Composable
fun PostDetailsContent(
    state: PostDetailsState,
    onContinueClick: () -> Unit,
    onViewHistoryClick: () -> Unit,
    onMoreClick: () -> Unit,
    onUserClick: (userId: String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {

    val post = state.postUi
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PostDetailsTopBar(
            onBackClick = onBackClick,
            onMoreClick = onMoreClick,
            isLoading = post == null,
        )


        ShimmerContent(
            isLoading = post == null,
            shimmerContent = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PostDetailsElementShimmer(
                        content = PostContent.Text(""),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Spacer(modifier = Modifier.weight(1f))

                    PostDetailsButtonShimmer()

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "You have 60 seconds to draw!",
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier.shimmer(cornerRadius = 4.dp)
                    )

                    Spacer(modifier = Modifier
                        .height(32.dp)
                        .navigationBarsPadding())
                }
            },
            content = {
                if(post != null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PostDetailsElement(
                            post = post,
                            onUserClick = { onUserClick(post.authorId) },
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Spacer(modifier = Modifier.weight(1f))

                        PostDetailsButton(
                            buttonType = state.buttonType,
                            isLoading = state.isContinueLoading,
                            onContinueClick = onContinueClick,
                            onViewHistoryClick = onViewHistoryClick,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        val descriptionText = when (state.buttonType) {
                            PostDetailsButtonType.COOLDOWN ->
                                stringResource(state.buttonType.descriptionResId, state.formattedCooldown)
                            PostDetailsButtonType.MY_SESSION ->
                                stringResource(state.buttonType.descriptionResId, state.formattedSessionTime)
                            else ->
                                stringResource(state.buttonType.descriptionResId, post.nextTimeLimit)
                        }

                        Text(
                            text = descriptionText,
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier
                            .height(32.dp)
                            .navigationBarsPadding())
                    }
                }
            }
        )

    }

}

@Preview
@Composable
fun PostDetailsContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            PostDetailsContent(
                state = PostDetailsState(
                    postUi = PostUi(
                        id = "2",
                        authorId = "user-1",
                        authorName = "Alex",
                        avatarUrl = null,
                        content = PostContent.Drawing(),
//                        content = PostContent.Text("Once upon a time there was a broken telephone that nobody could fix..."),
                        createdAt = System.currentTimeMillis() - 7200000,
                        generation = 10,
                        maxGenerations = 10,
                        status = PostStatus.AVAILABLE,
                        nextTimeLimit = 30,
                    ),
//                    MockPostRepository.mockList[2].toUi().copy(
//                        status = PostStatus.AVAILABLE,
//                        generation = 4,
//                        maxGenerations = 10
//                    ),
//                    isContinueLoading = false
                ),
                onContinueClick = {},
                onViewHistoryClick = {},
                onMoreClick = {},
                onUserClick = {}
            )
        }
    }
}
