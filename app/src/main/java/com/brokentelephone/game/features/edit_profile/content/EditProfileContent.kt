package com.brokentelephone.game.features.edit_profile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.EditProfileTopBar
import com.brokentelephone.game.features.edit_profile.model.EditProfileState

@Composable
fun EditProfileContent(
    state: EditProfileState,
    onBackClick: () -> Unit,
    onEditPhotoClick: () -> Unit,
    onEditUsernameClick: () -> Unit,
    onEditEmailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = stringResource(R.string.edit_profile_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        AccountAvatarInfoItem(
            name = stringResource(R.string.edit_profile_field_avatar),
            avatarUrl = state.user?.avatarUrl,
            modifier = Modifier.clickable(onClick = onEditPhotoClick).padding(horizontal = 16.dp)
        )

//        HorizontalDivider(color = MaterialTheme.appColors.divider)

        AccountTextInfoItem(
            name = stringResource(R.string.edit_profile_field_username),
            value = state.user?.username.orEmpty(),
            modifier = Modifier.clickable(onClick = onEditUsernameClick).padding(horizontal = 16.dp)
        )

//        HorizontalDivider(color = MaterialTheme.appColors.divider)

        AccountTextInfoItem(
            name = stringResource(R.string.edit_profile_field_email),
            value = state.user?.email.orEmpty(),
            modifier = Modifier.clickable(onClick = onEditEmailClick).padding(horizontal = 16.dp)
        )

    }
}

@Preview
@Composable
fun EditProfileContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        EditProfileContent(
            state = EditProfileState(
                user = UserUi(
                    id = "user-1",
                    username = "Alex",
                    email = "alex@example.com",
                    avatarUrl = null,
                    createdAt = 0
                )
            ),
            onBackClick = {},
            onEditPhotoClick = {},
            onEditUsernameClick = {},
            onEditEmailClick = {},
        )
    }
}
