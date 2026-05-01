package com.brokentelephone.game.features.choose_username.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.button.AuthButton
import com.brokentelephone.game.core.composable.chip.UsernameChip
import com.brokentelephone.game.core.composable.text_field.SignUpTextFieldValue
import com.brokentelephone.game.core.composable.top_bar.AuthTopBar
import com.brokentelephone.game.core.model.user.SuggestedUsernames
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.choose_username.ChooseUsernameViewModel
import com.brokentelephone.game.features.choose_username.model.ChooseUsernameState
import kotlinx.coroutines.delay

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
    val usernameFocus = remember { FocusRequester() }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(state.username, TextRange(state.username.length))
        )
    }

    LaunchedEffect(Unit) {
        delay(150)
        usernameFocus.requestFocus()
    }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .imePadding(),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SignUpTextFieldValue(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    onUsernameChange(newValue.text)
                },
                modifier = Modifier
                    .focusRequester(usernameFocus),
                label = stringResource(R.string.edit_username_label),
                hint = stringResource(R.string.edit_username_hint),
                onClearClick = if (state.username.isNotBlank()) {
                    {
                        textFieldValue = TextFieldValue("")
                        onUsernameChange("")
                    }
                } else null,
                onImeAction = {
                    focusManager.clearFocus()
                    onContinueClick()
                },
                maxLength = ChooseUsernameViewModel.MAX_USERNAME_LENGTH,
            )

            FlowRow(modifier = Modifier) {
                state.suggestions.forEach { name ->
                    UsernameChip(
                        name = name,
                        onClick = {
                            textFieldValue = TextFieldValue(name, TextRange(name.length))
                            onUsernameChange(name)
                            usernameFocus.requestFocus()
                        },
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
                isLoading = state.isLoading,
                modifier = Modifier.padding(vertical = 16.dp),
            )

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Preview
@Composable
fun ChooseUsernameContentPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
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
