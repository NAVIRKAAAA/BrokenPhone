package com.broken.telephone.features.profile

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
import com.broken.telephone.features.bottom_nav_bar.AppNavBottomBarViewModel
import com.broken.telephone.features.profile.content.ProfileContent
import com.broken.telephone.features.profile.model.ProfileSideEffect
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onPostClick: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    viewModel: ProfileViewModel = koinViewModel(),
    navBarViewModel: AppNavBottomBarViewModel = koinInject(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                ProfileSideEffect.ShowReportSuccessToast ->
                    Toast.makeText(context, "Thank you for your report!", Toast.LENGTH_SHORT).show()
                ProfileSideEffect.ShowNotInterestedToast ->
                    Toast.makeText(context, "Got it! We'll show you fewer posts like this.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    ProfileContent(
        state = state,
        onEditClick = onEditClick,
        onSettingsClick = onSettingsClick,
        onTabSelect = viewModel::onTabSelect,
        onScrollDirectionChange = navBarViewModel::onScrollDirectionChange,
        onPostClick = onPostClick,
        onMoreClick = { postId ->
            val post = (state.myPosts + state.myContributions).find { it.id == postId } ?: return@ProfileContent
            viewModel.onMoreClick(post)
        },
        modifier = modifier,
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
                    PostBottomSheetAction.COPY_LINK -> {}
                    PostBottomSheetAction.BLOCK -> viewModel.onBlockClick()
                    PostBottomSheetAction.REPORT -> viewModel.onReportClick()
                    PostBottomSheetAction.DELETE -> {}
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
}
