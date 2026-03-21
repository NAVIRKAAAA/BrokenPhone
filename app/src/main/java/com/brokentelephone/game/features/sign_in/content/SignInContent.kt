package com.brokentelephone.game.features.sign_in.content

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.button.AuthButton
import com.brokentelephone.game.core.text_field.SignUpTextField
import com.brokentelephone.game.core.text_field.SignUpTextFieldValue
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.AuthTopBar
import com.brokentelephone.game.features.sign_in.model.SignInState
import kotlinx.coroutines.delay

@Composable
fun SignInContent(
    state: SignInState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onEmailChanged: (TextFieldValue) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onTogglePasswordVisibility: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
) {
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        delay(150)
        emailFocus.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        AuthTopBar(
            title = stringResource(R.string.sign_in_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignUpTextFieldValue(
                value = state.email,
                onValueChange = onEmailChanged,
                label = stringResource(R.string.sign_in_email),
                error = if (state.credentialsError != null) "" else null,
                imeAction = ImeAction.Next,
                onImeAction = { passwordFocus.requestFocus() },
                modifier = Modifier.focusRequester(emailFocus),
            )

            Spacer(modifier = Modifier.height(8.dp))

            SignUpTextField(
                text = state.password,
                onTextChange = onPasswordChanged,
                label = stringResource(R.string.sign_in_password),
                error = state.credentialsError,
                isPasswordVisible = state.isPasswordVisible,
                onPasswordVisibilityToggle = onTogglePasswordVisibility,
                imeAction = ImeAction.Done,
                onImeAction = {
                    focusManager.clearFocus()
                    onSignInClick()
                },
                modifier = Modifier.focusRequester(passwordFocus),
            )

            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(stringResource(R.string.sign_in_forgot_password))
            }

            Spacer(modifier = Modifier.height(8.dp))

            AuthButton(
                text = stringResource(R.string.sign_in_button),
                onClick = onSignInClick,
                enabled = state.isSignInEnabled,
                isLoading = state.isLoading,
            )

            Spacer(modifier = Modifier.height(16.dp))

            val primaryColor = MaterialTheme.colorScheme.primary
            val signUpText = buildAnnotatedString {
                append(stringResource(R.string.sign_in_dont_have_account))
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "SIGN_UP",
                        linkInteractionListener = { onSignUpClick() },
                    )
                ) {
                    withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.None)) {
                        append(" ${stringResource(R.string.sign_in_sign_up_link)}")
                    }
                }
            }

            Text(
                text = signUpText,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

        }
    }
}

@Preview
@Composable
fun SignInContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        SignInContent(
            state = SignInState(
                credentialsError = ""
            ),
            onBackClick = {},
        )
    }
}
