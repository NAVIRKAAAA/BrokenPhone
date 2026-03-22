package com.brokentelephone.game.features.forgot_password.content

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.button.AuthButton
import com.brokentelephone.game.core.text_field.SignUpTextFieldValue
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.top_bar.AuthTopBar
import com.brokentelephone.game.features.forgot_password.R
import com.brokentelephone.game.features.forgot_password.model.ForgotPasswordState
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordContent(
    state: ForgotPasswordState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onEmailChanged: (TextFieldValue) -> Unit = {},
    onSendClick: () -> Unit = {},
) {
    val emailFocus = remember { FocusRequester() }
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
            title = stringResource(R.string.forgot_password_title),
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
            SignUpTextFieldValue(
                value = state.email,
                onValueChange = onEmailChanged,
                label = stringResource(R.string.forgot_password_email),
                hint = stringResource(R.string.forgot_password_subtitle),
                imeAction = ImeAction.Done,
                onImeAction = {
                    focusManager.clearFocus()
                    onSendClick()
                },
                modifier = Modifier.focusRequester(emailFocus)
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = stringResource(R.string.forgot_password_send_button),
                onClick = onSendClick,
                enabled = state.isSendEnabled,
                isLoading = state.isLoading,
            )

//            Spacer(modifier = Modifier.height(16.dp))

//            val primaryColor = MaterialTheme.colorScheme.primary
//            val signInText = buildAnnotatedString {
//                append("${stringResource(R.string.forgot_password_remember_password)} ")
//                withLink(
//                    LinkAnnotation.Clickable(
//                        tag = "SIGN_IN",
//                        linkInteractionListener = { onBackClick() },
//                    )
//                ) {
//                    withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.None)) {
//                        append(stringResource(R.string.forgot_password_sign_in_link))
//                    }
//                }
//            }
//
//            Text(
//                text = signInText,
//                textAlign = TextAlign.Center,
//                fontFamily = FontFamily(Font(CoreR.font.nunito_semi_bold)),
//                fontSize = 14.sp,
//                lineHeight = 21.sp,
//                color = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.padding(horizontal = 16.dp),
//            )
        }
    }
}

@Preview()
@Composable
fun ForgotPasswordContentPreview() {
    BrokenTelephoneTheme {
        ForgotPasswordContent(
            state = ForgotPasswordState(email = TextFieldValue("user@example.com")),
            onBackClick = {},
        )
    }
}
