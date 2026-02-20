package com.broken.telephone.features.sign_up.content

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.sign_up.model.SignUpState

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
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        SignUpTopBar(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(16.dp))

        val passwordFocus = remember { FocusRequester() }
        val confirmPasswordFocus = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

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
                label = "Email",
                error = state.emailError,
                imeAction = ImeAction.Next,
                onImeAction = { passwordFocus.requestFocus() },
            )

            Spacer(modifier = Modifier.height(12.dp))

            SignUpTextField(
                text = state.password,
                onTextChange = onPasswordChanged,
                label = "Password",
                error = state.passwordError,
                hint = "At least 8 characters",
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
                label = "Confirm Password",
                error = state.confirmPasswordError,
                isPasswordVisible = state.isConfirmPasswordVisible,
                onPasswordVisibilityToggle = onToggleConfirmPasswordVisibility,
                imeAction = ImeAction.Done,
                onImeAction = { focusManager.clearFocus() },
                modifier = Modifier.focusRequester(confirmPasswordFocus),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSignUpClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = state.isSignUpEnabled,
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
                        text = "Sign Up",
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(16.dp))

            val primaryColor = MaterialTheme.colorScheme.primary
            val termsText = buildAnnotatedString {
                append("By signing up, you agree to our ")
                withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.Underline)) {
                    append("Terms")
                }
                append(" & ")
                withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.Underline)) {
                    append("Privacy Policy")
                }
            }

            Text(
                text = termsText,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFF999999)
            )

            Spacer(modifier = Modifier.height(26.dp))

            val signInText = buildAnnotatedString {
                append("Already have an account? ")
                withStyle(SpanStyle(color = primaryColor)) {
                    append("Sign In")
                }
            }

            Text(
                text = signInText,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun SignUpContentPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        SignUpContent(
            state = SignUpState(
                isLoading = true
            ),
            onBackClick = {},
        )
    }
}
