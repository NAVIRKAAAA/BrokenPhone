package com.brokentelephone.game.features.edit_username.content

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.SaveTopBar
import com.brokentelephone.game.features.edit_username.EditUsernameViewModel
import com.brokentelephone.game.features.edit_username.model.EditUsernameState
import com.brokentelephone.game.features.sign_up.content.SignUpTextField

@Composable
fun EditUsernameContent(
    state: EditUsernameState,
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
        SaveTopBar(
            title = stringResource(R.string.edit_username_title),
            saveButtonText = stringResource(R.string.edit_username_button_save),
            isSaveEnabled = state.isSaveEnabled,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SignUpTextField(
            text = state.username,
            onTextChange = onUsernameChange,
            modifier = Modifier.padding(horizontal = 16.dp),
            label = stringResource(R.string.edit_username_label),
            hint = stringResource(R.string.edit_username_hint),
            onClearClick = if (state.username.isNotBlank()) { { onUsernameChange("") } } else null,
            onImeAction = { focusManager.clearFocus() },
            maxLength = EditUsernameViewModel.MAX_USERNAME_LENGTH
        )
    }
}

@Preview
@Composable
fun EditUsernameContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        EditUsernameContent(
            state = EditUsernameState(),
            onBackClick = {},
            onSaveClick = {},
            onUsernameChange = {},
        )
    }
}
