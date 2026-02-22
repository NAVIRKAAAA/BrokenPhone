package com.broken.telephone.features.post_details

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.core.bottom_sheet.post_bottom_sheet.PostBottomSheet
import com.broken.telephone.core.bottom_sheet.post_bottom_sheet.model.PostBottomSheetAction
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.ReportPostBottomSheet
import com.broken.telephone.core.dialog.ConfirmDialog
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
                is PostDetailsSideEffect.ShowCopyLinkSuccessToast -> {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("post_link", effect.link))
                    Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show()
                }
                PostDetailsSideEffect.NavigateBack -> onBackClick()
                is PostDetailsSideEffect.NavigateToDraw -> onDrawContinue(effect.postId)
                is PostDetailsSideEffect.NavigateToDescribeDrawing -> onDescribeDrawingContinue(effect.postId)
            }
        }
    }

    PostDetailsContent(
        state = state,
        onBackClick = onBackClick,
        onMoreClick = viewModel::onMoreClick,
        onContinueClick = viewModel::onContinueClick,
        onViewHistoryClick = {
            state.postUi?.let { onViewHistoryClick(it.id) }
        },
        modifier = modifier
    )

    if (state.isBottomSheetVisible) {
        val actions = if (state.isCurrentUserPost) {
            listOf(PostBottomSheetAction.COPY_LINK, PostBottomSheetAction.DELETE)
        } else {
            PostBottomSheetAction.entries.filter { it != PostBottomSheetAction.DELETE }
        }
        PostBottomSheet(
            onDismissRequest = viewModel::onBottomSheetDismiss,
            actions = actions,
            onActionClick = { action ->
                when (action) {
                    PostBottomSheetAction.NOT_INTERESTED -> viewModel.onNotInterestedClick()
                    PostBottomSheetAction.COPY_LINK -> viewModel.onCopyLinkClick()
                    PostBottomSheetAction.BLOCK -> viewModel.onBlockClick()
                    PostBottomSheetAction.REPORT -> viewModel.onReportClick()
                    PostBottomSheetAction.DELETE -> viewModel.onDeleteClick()
                }
            },
        )
    }

    if (state.isBlockDialogVisible) {
        ConfirmDialog(
            title = "Block user?",
            body = "You won't see posts from this user anymore.",
            cancelText = "Cancel",
            confirmText = "Block",
            onDismiss = viewModel::onBlockDialogDismiss,
            onConfirm = viewModel::onBlockConfirm,
            isLoading = state.isBlockLoading,
        )
    }

    if (state.isDeleteDialogVisible) {
        ConfirmDialog(
            title = "Delete post?",
            body = "This action cannot be undone.",
            cancelText = "Cancel",
            confirmText = "Delete",
            onDismiss = viewModel::onDeleteDialogDismiss,
            onConfirm = viewModel::onDeleteConfirm,
            isLoading = state.isDeleteLoading,
        )
    }

    if (state.isReportBottomSheetVisible) {
        ReportPostBottomSheet(
            onDismissRequest = viewModel::onReportBottomSheetDismiss,
            onReportClick = viewModel::onReportTypeSelected,
        )
    }
}
