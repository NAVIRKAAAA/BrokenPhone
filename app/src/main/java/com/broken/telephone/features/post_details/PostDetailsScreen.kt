package com.broken.telephone.features.post_details

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.core.bottom_sheet.post_bottom_sheet.PostBottomSheet
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.ReportPostBottomSheet
import com.broken.telephone.core.dialog.BlockUserDialog
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.features.post_details.content.PostDetailsContent
import com.broken.telephone.features.post_details.model.PostDetailsSideEffect
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    onDrawContinue: (postId: String) -> Unit,
    onDescribeDrawingContinue: (postId: String) -> Unit,
    onViewHistoryClick: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: PostDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                PostDetailsSideEffect.ShowReportSuccessToast ->
                    Toast.makeText(context, "Thank you for your report!", Toast.LENGTH_SHORT).show()
                PostDetailsSideEffect.NavigateBack -> onBackClick()
            }
        }
    }

    PostDetailsContent(
        state = state,
        onBackClick = onBackClick,
        onMoreClick = viewModel::onMoreClick,
        onContinueClick = {
            val post = state.postUi ?: return@PostDetailsContent
            when (post.content) {
                is PostContent.Text -> onDrawContinue(post.id)
                is PostContent.Drawing -> onDescribeDrawingContinue(post.id)
            }
        },
        onViewHistoryClick = {
            state.postUi?.let { onViewHistoryClick(it.id) }
        },
        modifier = modifier
    )

    if (state.isBottomSheetVisible) {
        PostBottomSheet(
            onDismissRequest = viewModel::onBottomSheetDismiss,
            onNotInterestedClick = viewModel::onNotInterestedClick,
            onReportClick = viewModel::onReportClick,
            onBlockClick = viewModel::onBlockClick,
        )
    }

    if (state.isBlockDialogVisible) {
        BlockUserDialog(
            onDismiss = viewModel::onBlockDialogDismiss,
            onBlockClick = viewModel::onBlockConfirm,
            isLoading = state.isBlockLoading,
        )
    }

    if (state.isReportBottomSheetVisible) {
        ReportPostBottomSheet(
            onDismissRequest = viewModel::onReportBottomSheetDismiss,
            onReportClick = viewModel::onReportTypeSelected,
        )
    }
}
