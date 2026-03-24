package com.brokentelephone.game.features.dashboard

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
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
import com.brokentelephone.game.features.bottom_nav_bar.AppNavBottomBarViewModel
import com.brokentelephone.game.features.bottom_nav_bar.model.BottomNavBarEvent
import com.brokentelephone.game.features.dashboard.content.DashboardContent
import com.brokentelephone.game.features.dashboard.model.DashboardSideEffect
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@SuppressLint("LocalContextGetResourceValueCall")
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
    val scope = rememberCoroutineScope()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadInitialPosts()
    }

    LaunchedEffect(Unit) {
        navBarViewModel.event.collect { event ->
            if (event is BottomNavBarEvent.ScrollToTopDashboard) {
                listState.animateScrollToItem(0)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                DashboardSideEffect.ShowReportSuccessToast ->
                    Toast.makeText(
                        context,
                        context.getString(R.string.common_toast_report_success),
                        Toast.LENGTH_SHORT
                    ).show()

                DashboardSideEffect.ShowNotInterestedToast ->
                    Toast.makeText(
                        context,
                        context.getString(R.string.common_toast_not_interested),
                        Toast.LENGTH_SHORT
                    ).show()

                is DashboardSideEffect.ShowCopyLinkSuccessToast -> {
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("post_link", effect.link))
                    Toast.makeText(
                        context,
                        context.getString(R.string.common_toast_link_copied),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                DashboardSideEffect.ScrollToTop -> {
                   listState.animateScrollToItem(0)
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
        onRefresh = viewModel::onRefresh,
        onLoadMore = viewModel::loadNextPosts,
        onTitleClick = { scope.launch { listState.animateScrollToItem(0) } },
        modifier = modifier,
        onSortSelected = viewModel::onSortSelected
    )

    if (state.isPostBottomSheetVisible) {
        PostBottomSheet(
            onDismissRequest = viewModel::onPostBottomSheetDismiss,
            actions = PostBottomSheetAction.entries.filter { it != PostBottomSheetAction.DELETE },
            onActionClick = { action ->
                when (action) {
                    PostBottomSheetAction.NOT_INTERESTED -> viewModel.onNotInterestedClick()
                    PostBottomSheetAction.COPY_LINK -> viewModel.onCopyLinkClick()
                    PostBottomSheetAction.BLOCK -> viewModel.onBlockClick()
                    PostBottomSheetAction.REPORT -> viewModel.onReportClick()
                    else -> return@PostBottomSheet
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

    state.globalError?.let { error ->
        ErrorDialog(
            body = error,
            onOkClick = viewModel::onGlobalErrorDismiss,
        )
    }
}
