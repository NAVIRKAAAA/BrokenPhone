package com.broken.telephone.features.post_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.features.post_details.content.PostDetailsContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PostDetailsScreen(
    onDrawContinue: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: PostDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PostDetailsContent(
        state = state,
        onBackClick = onBackClick,
        onContinueClick = {
            val post = state.postUi ?: return@PostDetailsContent
            when (post.content) {
                is PostContent.Text -> onDrawContinue(post.id)
                is PostContent.Drawing -> Unit
            }
        },
        modifier = modifier
    )
}
