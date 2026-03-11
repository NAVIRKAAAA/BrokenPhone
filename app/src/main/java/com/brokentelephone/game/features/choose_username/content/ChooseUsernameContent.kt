package com.brokentelephone.game.features.choose_username.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.brokentelephone.game.core.button.AuthButton
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.AuthTopBar
import com.brokentelephone.game.features.choose_username.ChooseUsernameViewModel
import com.brokentelephone.game.features.choose_username.model.ChooseUsernameState
import com.brokentelephone.game.features.choose_username.model.SuggestedUsernames
import com.brokentelephone.game.features.sign_up.content.SignUpTextField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseUsernameContent(
    state: ChooseUsernameState,
    onBackClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        AuthTopBar(
            title = stringResource(R.string.choose_username_title),
            onBackClick = onBackClick,
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
            maxLength = ChooseUsernameViewModel.MAX_USERNAME_LENGTH,
        )

        FlowRow(modifier = Modifier.padding(horizontal = 16.dp)) {
            state.suggestions.forEach { name ->
                UsernameChip(
                    name = name,
                    onClick = { onUsernameChange(name) },
                    enabled = state.username != name,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.weight(1f))

        AuthButton(
            text = stringResource(R.string.choose_username_button_continue),
            onClick = onContinueClick,
            enabled = state.isContinueEnabled,
            isLoading = false,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        )

        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Preview
@Composable
fun ChooseUsernameContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        val suggestions = SuggestedUsernames.random10()
        ChooseUsernameContent(
            state = ChooseUsernameState(
                username = suggestions[2],
                suggestions = suggestions
            ),
            onBackClick = {},
            onUsernameChange = {},
            onContinueClick = {},
        )
    }
}
