package com.brokentelephone.game.features.edit_username.content

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
import com.brokentelephone.game.core.text_field.SignUpTextFieldValue
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.SaveTopBar
import com.brokentelephone.game.features.choose_username.content.UsernameChip
import com.brokentelephone.game.features.choose_username.model.SuggestedUsernames
import com.brokentelephone.game.features.edit_username.EditUsernameViewModel
import com.brokentelephone.game.features.edit_username.model.EditUsernameState
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditUsernameContent(
    state: EditUsernameState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onUsernameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val usernameFocus = remember { FocusRequester() }
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(state.username, TextRange(state.username.length)))
    }

    LaunchedEffect(Unit) {
        delay(150)
        usernameFocus.requestFocus()
    }

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
            isLoading = state.isLoading,
            onBackClick = onBackClick,
            onSaveClick = {
                focusManager.clearFocus()
                onSaveClick()
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                    .padding(horizontal = 16.dp)
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
                    onSaveClick()
                },
                maxLength = EditUsernameViewModel.MAX_USERNAME_LENGTH,
            )

            FlowRow(modifier = Modifier.padding(horizontal = 16.dp)) {
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

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Preview
@Composable
fun EditUsernameContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        val suggestions = SuggestedUsernames.random10()
        EditUsernameContent(
            state = EditUsernameState(
                username = suggestions[0],
                suggestions = suggestions,
            ),
            onBackClick = {},
            onSaveClick = {},
            onUsernameChange = {},
        )
    }
}
