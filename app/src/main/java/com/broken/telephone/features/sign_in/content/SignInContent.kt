package com.broken.telephone.features.sign_in.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.sign_in.model.SignInState
import com.broken.telephone.features.sign_up.content.SignUpTextField

@Composable
fun SignInContent(
    state: SignInState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onTogglePasswordVisibility: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
) {
    val passwordFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
    ) {
        SignInTopBar(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignUpTextField(
                text = state.email,
                onTextChange = onEmailChanged,
                label = stringResource(R.string.sign_in_email),
                error = if (state.credentialsError != null) "" else null,
                imeAction = ImeAction.Next,
                onImeAction = { passwordFocus.requestFocus() },
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
                onImeAction = { focusManager.clearFocus() },
                modifier = Modifier.focusRequester(passwordFocus),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = state.isSignInEnabled,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.sign_in_button),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

        }
    }
}

@Preview
@Composable
fun SignInContentPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        SignInContent(
            state = SignInState(),
            onBackClick = {},
        )
    }
}
