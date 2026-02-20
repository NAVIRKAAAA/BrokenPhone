package com.broken.telephone.features.edit_profile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.edit_profile.model.EditProfileState
import com.broken.telephone.features.sign_up.content.SignUpTextField

@Composable
fun EditProfileContent(
    state: EditProfileState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            isSaveEnabled = state.isSaveEnabled,
            onCloseClick = onBackClick,
            onSaveClick = onSaveClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SignUpTextField(
            text = state.username,
            onTextChange = onUsernameChange,
            modifier = Modifier.padding(horizontal = 16.dp),
            label = "Username",
            hint = "This is how others will see you",
            onImeAction = { focusManager.clearFocus() },
        )
    }
}

@Preview
@Composable
fun EditProfileContentPreview() {
    BrokenTelephoneTheme {
        EditProfileContent(
            state = EditProfileState(),
            onBackClick = {},
            onSaveClick = {},
            onUsernameChange = {},
        )
    }
}
