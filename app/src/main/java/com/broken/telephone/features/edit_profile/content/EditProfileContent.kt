package com.broken.telephone.features.edit_profile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.edit_profile.model.EditProfileState
import com.broken.telephone.features.profile.model.UserUi

@Composable
fun EditProfileContent(
    state: EditProfileState,
    onBackClick: () -> Unit,
    onEditPhotoClick: () -> Unit,
    onEditUsernameClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(24.dp))

        AccountAvatarInfoItem(
            name = "Avatar",
            avatarUrl = state.user?.avatarUrl,
            modifier = Modifier.clickable(onClick = onEditPhotoClick).padding(horizontal = 16.dp)
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        AccountTextInfoItem(
            name = "Username",
            value = state.user?.username.orEmpty(),
            modifier = Modifier.clickable(onClick = onEditUsernameClick).padding(horizontal = 16.dp)
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        AccountTextInfoItem(
            name = "Email",
            value = state.user?.email.orEmpty(),
            enabled = false,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Preview
@Composable
fun EditProfileContentPreview() {
    BrokenTelephoneTheme {
        EditProfileContent(
            state = EditProfileState(
                user = UserUi(
                    id = "user-1",
                    username = "Alex",
                    email = "alex@example.com",
                    avatarUrl = null
                )
            ),
            onBackClick = {},
            onEditPhotoClick = {},
            onEditUsernameClick = {},
        )
    }
}
