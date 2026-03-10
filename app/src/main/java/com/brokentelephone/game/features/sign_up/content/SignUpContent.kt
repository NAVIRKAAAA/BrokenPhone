package com.brokentelephone.game.features.sign_up.content

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.button.AuthButton
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.AuthTopBar
import com.brokentelephone.game.features.sign_up.model.SignUpState
import kotlinx.coroutines.delay

@Composable
fun SignUpContent(
    state: SignUpState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onTogglePasswordVisibility: () -> Unit = {},
    onToggleConfirmPasswordVisibility: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        AuthTopBar(
            title = stringResource(R.string.sign_up_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        val emailFocus = remember { FocusRequester() }
        val passwordFocus = remember { FocusRequester() }
        val confirmPasswordFocus = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        LaunchedEffect(Unit) {
            delay(150)
            emailFocus.requestFocus()
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignUpTextField(
                text = state.email,
                onTextChange = onEmailChanged,
                label = stringResource(R.string.sign_up_email),
                error = state.emailError,
                imeAction = ImeAction.Next,
                onImeAction = { passwordFocus.requestFocus() },
                modifier = Modifier.focusRequester(emailFocus),
            )

            Spacer(modifier = Modifier.height(12.dp))

            SignUpTextField(
                text = state.password,
                onTextChange = onPasswordChanged,
                label = stringResource(R.string.sign_up_password),
                error = state.passwordError,
                hint = stringResource(R.string.sign_up_password_hint),
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
                label = stringResource(R.string.sign_up_confirm_password),
                error = state.confirmPasswordError,
                isPasswordVisible = state.isConfirmPasswordVisible,
                onPasswordVisibilityToggle = onToggleConfirmPasswordVisibility,
                imeAction = ImeAction.Done,
                onImeAction = {
                    focusManager.clearFocus()
                    onSignUpClick()
                },
                modifier = Modifier.focusRequester(confirmPasswordFocus),
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = stringResource(R.string.sign_up_button),
                onClick = onSignUpClick,
                enabled = state.isSignUpEnabled,
                isLoading = state.isLoading,
            )

            Spacer(modifier = Modifier.height(26.dp))
            val primaryColor = MaterialTheme.colorScheme.primary

            val signInText = buildAnnotatedString {
                append("${stringResource(R.string.sign_up_already_have_account)} ")
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "SIGN_IN",
                        linkInteractionListener = { onSignInClick() },
                    )
                ) {
                    withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.None)) {
                        append(stringResource(R.string.sign_up_sign_in_link))
                    }
                }
            }

            Text(
                text = signInText,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(16.dp))

            val termsText = buildAnnotatedString {
                append("${stringResource(R.string.sign_up_terms_prefix)} ")
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "TERMS",
                        linkInteractionListener = { onTermsClick() },
                    )
                ) {
                    withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.None)) {
                        append(stringResource(R.string.sign_up_terms))
                    }
                }
                append(" ${stringResource(R.string.sign_up_terms_and)} ")
                withLink(
                    LinkAnnotation.Clickable(
                        tag = "PRIVACY",
                        linkInteractionListener = { onPrivacyPolicyClick() },
                    )
                ) {
                    withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.None)) {
                        append(stringResource(R.string.sign_up_privacy_policy))
                    }
                }
            }

            Text(
                text = termsText,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun SignUpContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        SignUpContent(
            state = SignUpState(
                isLoading = false,
                email = "sa",
                password = "sa",
                confirmPassword = "sa"
            ),
            onBackClick = {},
        )
    }
}
