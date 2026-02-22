package com.broken.telephone.features.post_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
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
            val buttonType = when {
                post.isCompleted -> PostDetailsButtonType.COMPLETED
                state.isCurrentUserPost -> PostDetailsButtonType.OWN_POST
                post.status != PostStatus.AVAILABLE -> PostDetailsButtonType.UNAVAILABLE
                post.content is PostContent.Text -> PostDetailsButtonType.DRAW
                else -> PostDetailsButtonType.DESCRIBE
            }

            PostDetailsElement(
                post = post,
                isUsersPost = state.isCurrentUserPost,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            val containerColor = if (buttonType == PostDetailsButtonType.COMPLETED) {
                Color(0xFF22C55E)
            } else {
                MaterialTheme.colorScheme.primary
            }

            Button(
                onClick = if (buttonType == PostDetailsButtonType.COMPLETED) onViewHistoryClick else onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = containerColor,
                ),
                enabled = buttonType.isEnabled && !state.isContinueLoading,
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(),
            ) {
                if (state.isContinueLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp),
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = buttonType.buttonText,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = buttonType.description(post.nextTimeLimit),
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color.Gray,
                maxLines = 1,
            )
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
                    status = PostStatus.COMPLETED,
                    generation = 10,
                    maxGenerations = 10
                ),
                isContinueLoading = true
            ),
            onContinueClick = {},
            onViewHistoryClick = {},
            onMoreClick = {}
        )
    }
}
