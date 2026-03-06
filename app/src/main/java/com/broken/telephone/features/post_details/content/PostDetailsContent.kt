package com.broken.telephone.features.post_details.content

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.data.repository.MockPostRepository
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.features.dashboard.model.toUi
import com.broken.telephone.features.post_details.model.PostDetailsButtonType
import com.broken.telephone.features.post_details.model.PostDetailsState

@Composable
fun PostDetailsContent(
    state: PostDetailsState,
    onContinueClick: () -> Unit,
    onViewHistoryClick: () -> Unit,
    onMoreClick: () -> Unit,
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
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (post != null) {
            PostDetailsElement(
                post = post,
                isUsersPost = state.isCurrentUserPost,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.weight(1f))

            val buttonType = when {
                post.isCompleted -> PostDetailsButtonType.COMPLETED
                state.isCurrentUserPost -> PostDetailsButtonType.OWN_POST
                post.status != PostStatus.AVAILABLE -> PostDetailsButtonType.UNAVAILABLE
                post.content is PostContent.Text -> PostDetailsButtonType.DRAW
                else -> PostDetailsButtonType.DESCRIBE
            }

            PostDetailsButton(
                buttonType = buttonType,
                isLoading = state.isContinueLoading,
                onContinueClick = onContinueClick,
                onViewHistoryClick = onViewHistoryClick,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(buttonType.descriptionResId, post.nextTimeLimit),
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color.Gray,
                maxLines = 1,
            )

            Spacer(modifier = Modifier.height(32.dp).navigationBarsPadding())
        }
    }

}

@Preview
@Composable
fun PostDetailsContentPreview() {
    BrokenTelephoneTheme() {
        PostDetailsContent(
            state = PostDetailsState(
                MockPostRepository.mockList.first().toUi().copy(
                    status = PostStatus.AVAILABLE,
                    generation = 4,
                    maxGenerations = 10
                ),
                isContinueLoading = false
            ),
            onContinueClick = {},
            onViewHistoryClick = {},
            onMoreClick = {}
        )
    }
}
