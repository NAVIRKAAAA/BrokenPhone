package com.brokentelephone.game.features.choose_avatar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.brokentelephone.game.features.choose_avatar.content.ChooseAvatarContent
import com.brokentelephone.game.features.choose_avatar.model.ChooseAvatarEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChooseAvatarScreen(
    onAvatarSelected: (avatarUrl: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChooseAvatarViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ChooseAvatarEvent.AvatarSelected -> onAvatarSelected(event.avatarUrl)
            }
        }
    }

    ChooseAvatarContent(
        onAvatarClick = viewModel::onAvatarClick,
        modifier = modifier,
    )
}
