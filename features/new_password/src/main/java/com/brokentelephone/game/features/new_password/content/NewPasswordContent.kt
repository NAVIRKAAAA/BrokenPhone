package com.brokentelephone.game.features.new_password.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.button.AuthButton
import com.brokentelephone.game.core.text_field.SignUpTextField
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.AuthTopBar
import com.brokentelephone.game.features.new_password.R
import com.brokentelephone.game.features.new_password.model.NewPasswordState
import kotlinx.coroutines.delay

@Composable
fun NewPasswordContent(
    state: NewPasswordState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onTogglePasswordVisibility: () -> Unit = {},
    onToggleConfirmPasswordVisibility: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    val passwordFocus = remember { FocusRequester() }
    val confirmPasswordFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        delay(150)
        passwordFocus.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        AuthTopBar(
            title = stringResource(R.string.new_password_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SignUpTextField(
                text = state.password,
                onTextChange = onPasswordChanged,
                label = stringResource(R.string.new_password_password),
                error = state.passwordError,
                hint = stringResource(com.brokentelephone.game.core.R.string.sign_up_password_hint),
                isPasswordVisible = state.isPasswordVisible,
                onPasswordVisibilityToggle = onTogglePasswordVisibility,
                imeAction = ImeAction.Next,
                onImeAction = { confirmPasswordFocus.requestFocus() },
                modifier = Modifier.focusRequester(passwordFocus),
            )

            Spacer(modifier = Modifier.height(8.dp))

            SignUpTextField(
                text = state.confirmPassword,
                onTextChange = onConfirmPasswordChanged,
                label = stringResource(R.string.new_password_confirm_password),
                error = state.confirmPasswordError,
                isPasswordVisible = state.isConfirmPasswordVisible,
                onPasswordVisibilityToggle = onToggleConfirmPasswordVisibility,
                imeAction = ImeAction.Done,
                onImeAction = {
                    focusManager.clearFocus()
                    onSaveClick()
                },
                modifier = Modifier.focusRequester(confirmPasswordFocus),
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = stringResource(R.string.new_password_save_button),
                onClick = onSaveClick,
                enabled = state.isSaveEnabled,
                isLoading = state.isLoading,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewPasswordContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        NewPasswordContent(
            state = NewPasswordState(
                confirmPasswordError = "Error"
            ),
            onBackClick = {},
        )
    }
}
