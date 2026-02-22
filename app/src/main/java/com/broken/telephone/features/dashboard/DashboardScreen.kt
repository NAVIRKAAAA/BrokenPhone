package com.broken.telephone.features.dashboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.core.bottom_sheet.post_bottom_sheet.PostBottomSheet
import com.broken.telephone.core.bottom_sheet.post_bottom_sheet.model.PostBottomSheetAction
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.ReportPostBottomSheet
import com.broken.telephone.core.dialog.ConfirmDialog
import com.broken.telephone.features.bottom_nav_bar.AppNavBottomBarViewModel
import com.broken.telephone.features.dashboard.content.DashboardContent
import com.broken.telephone.features.dashboard.model.DashboardSideEffect
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onPostClick: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
    navBarViewModel: AppNavBottomBarViewModel = koinInject()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            Log.d("LOG_TAG", "show toast")
            when (effect) {
                DashboardSideEffect.ShowReportSuccessToast ->
                    Toast.makeText(context, "Thank you for your report!", Toast.LENGTH_SHORT).show()
                DashboardSideEffect.ShowNotInterestedToast ->
                    Toast.makeText(context, "Got it! We'll show you fewer posts like this.", Toast.LENGTH_SHORT).show()
                is DashboardSideEffect.ShowCopyLinkSuccessToast -> {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("post_link", effect.link))
                    Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(listState) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousScrollOffset = listState.firstVisibleItemScrollOffset
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                val isScrollingUp = if (index != previousIndex) {
                    index < previousIndex
                } else {
                    offset <= previousScrollOffset
                }
                previousIndex = index
                previousScrollOffset = offset

                navBarViewModel.onScrollDirectionChange(isScrollingUp)
            }
    }

    DashboardContent(
        state = state,
        listState = listState,
        onPostClick = onPostClick,
        onMoreClick = { postId ->
            val post = state.posts.find { it.id == postId } ?: return@DashboardContent
            viewModel.onMoreClick(post)
        },
        modifier = modifier
    )

    if (state.isPostBottomSheetVisible) {
        val actions = if (state.isCurrentUserPost) {
            listOf(PostBottomSheetAction.COPY_LINK, PostBottomSheetAction.DELETE)
        } else {
            PostBottomSheetAction.entries.filter { it != PostBottomSheetAction.DELETE }
        }
        PostBottomSheet(
            onDismissRequest = viewModel::onPostBottomSheetDismiss,
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

    if (state.isReportBottomSheetVisible) {
        ReportPostBottomSheet(
            onDismissRequest = viewModel::onReportBottomSheetDismiss,
            onReportClick = viewModel::onReportTypeSelected,
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
}
