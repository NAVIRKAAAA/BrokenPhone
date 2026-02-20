package com.broken.telephone.features.edit_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.edit_profile.content.EditProfileContent
import com.broken.telephone.features.edit_profile.model.EditProfileEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                EditProfileEvent.NavigateBack -> onBackClick()
            }
        }
    }

    EditProfileContent(
        state = state,
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSave,
        onUsernameChange = viewModel::onUsernameChange,
        modifier = modifier,
    )
}
