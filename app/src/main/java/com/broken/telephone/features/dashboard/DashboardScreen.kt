package com.broken.telephone.features.dashboard

import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.bottom_nav_bar.AppNavBottomBarViewModel
import com.broken.telephone.features.dashboard.content.DashboardContent
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    onPostClick: (postId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
    navBarViewModel: AppNavBottomBarViewModel = koinInject()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

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

                Log.d("LOG_TAG", "isScrollingUp: $isScrollingUp")

                navBarViewModel.onScrollDirectionChange(isScrollingUp)
            }
    }

    DashboardContent(
        state = state,
        listState = listState,
        onPostClick = onPostClick,
        modifier = modifier
    )
}
