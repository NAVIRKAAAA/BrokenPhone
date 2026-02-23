package com.broken.telephone.features.post_details

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.R
import com.broken.telephone.core.bottom_sheet.post_bottom_sheet.PostBottomSheet
import com.broken.telephone.core.bottom_sheet.post_bottom_sheet.model.PostBottomSheetAction
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.ReportPostBottomSheet
import com.broken.telephone.core.dialog.ConfirmDialog
import com.broken.telephone.features.post_details.content.PostDetailsContent
import com.broken.telephone.features.post_details.model.PostDetailsSideEffect
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("LocalContextGetResourceValueCall")
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
                PostDetailsSideEffect.ShowReportSuccessToast ->  {
                    val text = context.getString(R.string.post_details_toast_report_success)
                    Toast.makeText(context,text , Toast.LENGTH_SHORT).show()
                }
                is PostDetailsSideEffect.ShowCopyLinkSuccessToast -> {
                    val text = context.getString(R.string.post_details_toast_link_copied)
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("post_link", effect.link))
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
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
            title = stringResource(R.string.post_details_dialog_block_title),
            body = stringResource(R.string.post_details_dialog_block_body),
            cancelText = stringResource(R.string.post_details_dialog_block_cancel),
            confirmText = stringResource(R.string.post_details_dialog_block_confirm),
            onDismiss = viewModel::onBlockDialogDismiss,
            onConfirm = viewModel::onBlockConfirm,
            isLoading = state.isBlockLoading,
        )
    }

    if (state.isDeleteDialogVisible) {
        ConfirmDialog(
            title = stringResource(R.string.post_details_dialog_delete_title),
            body = stringResource(R.string.post_details_dialog_delete_body),
            cancelText = stringResource(R.string.post_details_dialog_delete_cancel),
            confirmText = stringResource(R.string.post_details_dialog_delete_confirm),
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
