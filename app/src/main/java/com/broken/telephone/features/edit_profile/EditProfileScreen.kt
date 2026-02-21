package com.broken.telephone.features.edit_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.edit_profile.content.EditProfileContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onEditPhotoClick: () -> Unit,
    onEditUsernameClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditProfileContent(
        state = state,
        onBackClick = onBackClick,
        onEditPhotoClick = onEditPhotoClick,
        onEditUsernameClick = onEditUsernameClick,
        modifier = modifier,
    )
}
