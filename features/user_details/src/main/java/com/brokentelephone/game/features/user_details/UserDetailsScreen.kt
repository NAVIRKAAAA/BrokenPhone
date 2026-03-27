package com.brokentelephone.game.features.user_details

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.bottom_sheet.post_bottom_sheet.PostBottomSheet
import com.brokentelephone.game.core.bottom_sheet.report_post_bottom_sheet.ReportPostBottomSheet
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.core.model.bottom_shet.PostBottomSheetAction
import com.brokentelephone.game.features.user_details.content.UserDetailsContent
import com.brokentelephone.game.features.user_details.model.UserDetailsSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    userId: String,
    onBackClick: () -> Unit,
    onPostClick: (postId: String, userId: String) -> Unit,
    onNavigateBackWithForceUpdate: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: UserDetailsViewModel = koinViewModel { parametersOf(userId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                UserDetailsSideEffect.ShowReportSuccessToast ->
                    Toast.makeText(
                        context,
                        context.getString(R.string.common_toast_report_success),
                        Toast.LENGTH_SHORT
                    ).show()

                is UserDetailsSideEffect.ShowCopyLinkSuccessToast -> {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("post_link", effect.link))
                    Toast.makeText(
                        context,
                        context.getString(R.string.common_toast_link_copied),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                UserDetailsSideEffect.NavigateBack -> onBackClick()
                UserDetailsSideEffect.NavigateBackWithForceUpdate -> onNavigateBackWithForceUpdate()
            }
        }
    }

    UserDetailsContent(
        state = state,
        onBackClick = onBackClick,
        onTabSelect = viewModel::onTabSelect,
        onAddFriendClick = viewModel::onAddFriendClick,
        onPostClick = { postId -> onPostClick(postId, userId) },
        onMoreClick = { postId ->
            val post = (state.myPosts + state.myContributions).find { it.id == postId }
                ?: return@UserDetailsContent
            viewModel.onMoreClick(post)
        },
        modifier = modifier,
        onRefresh = viewModel::onRefresh,
    )

    if (state.isPostBottomSheetVisible) {
        val actions = if (state.isOwnProfile) {
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
            title = stringResource(R.string.common_dialog_block_title),
            body = stringResource(R.string.common_dialog_block_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.common_block),
            onDismiss = viewModel::onBlockDialogDismiss,
            onConfirm = viewModel::onBlockConfirm,
            isLoading = state.isBlockLoading,
        )
    }

    if (state.isDeleteDialogVisible) {
        ConfirmDialog(
            title = stringResource(R.string.common_dialog_delete_post_title),
            body = stringResource(R.string.common_dialog_delete_post_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.common_delete),
            onDismiss = viewModel::onDeleteDialogDismiss,
            onConfirm = viewModel::onDeleteConfirm,
            isLoading = state.isDeleteLoading,
        )
    }

    state.globalError?.let { error ->
        ErrorDialog(
            body = error,
            onOkClick = viewModel::onGlobalErrorDismiss,
        )
    }
}
