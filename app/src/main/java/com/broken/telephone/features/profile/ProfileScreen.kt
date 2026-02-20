package com.broken.telephone.features.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.bottom_nav_bar.AppNavBottomBarViewModel
import com.broken.telephone.features.profile.content.ProfileContent
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

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

    ProfileContent(
        state = state,
        onEditClick = onEditClick,
        onSettingsClick = onSettingsClick,
        onTabSelect = viewModel::onTabSelect,
        onScrollDirectionChange = navBarViewModel::onScrollDirectionChange,
        onPostClick = onPostClick,
        modifier = modifier,
    )
}
