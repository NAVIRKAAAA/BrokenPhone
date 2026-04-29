package com.brokentelephone.game.features.confirm_sign_up.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.button.AuthButton
import com.brokentelephone.game.core.composable.text_field.SignUpTextField
import com.brokentelephone.game.core.composable.top_bar.AuthTopBar
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.confirm_sign_up.model.ConfirmSignUpState
import com.brokentelephone.game.features.confirm_sign_up.model.OTP_CODE_LENGTH
import com.brokentelephone.game.features.confirm_sign_up.R as CoreR

@Composable
fun ConfirmSignUpContent(
    state: ConfirmSignUpState,
    onBackClick: () -> Unit,
    onCodeChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AuthTopBar(
            title = stringResource(CoreR.string.confirm_sign_up_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = buildAnnotatedString {
                    val regular = SpanStyle(fontFamily = FontFamily(Font(R.font.nunito_regular)))
                    val bold = SpanStyle(fontFamily = FontFamily(Font(R.font.nunito_bold)))
                    withStyle(regular) {
                        append("${stringResource(CoreR.string.confirm_sign_up_body_prefix, OTP_CODE_LENGTH)} ")
                    }
                    withStyle(bold) { append(state.email) }
                    withStyle(regular) {
                        append(stringResource(CoreR.string.confirm_sign_up_body_suffix))
                    }
                },
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(24.dp))

            SignUpTextField(
                text = state.code,
                onTextChange = onCodeChange,
                label = stringResource(CoreR.string.confirm_sign_up_code_label),
                hint = stringResource(CoreR.string.confirm_sign_up_code_hint, OTP_CODE_LENGTH),
                keyboardType = KeyboardType.Number,
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthButton(
                text = stringResource(CoreR.string.confirm_sign_up_confirm),
                onClick = onConfirmClick,
                enabled = state.isConfirmEnabled,
                isLoading = state.isConfirmLoading,
            )

            Spacer(modifier = Modifier.height(16.dp))

            val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
            val disabled = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            val baseStyle = SpanStyle(
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 14.sp,
                color = onSurfaceVariant,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(CoreR.string.confirm_sign_up_no_code),
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 14.sp,
                    color = onSurfaceVariant,
                )
                when {
                    state.isResendLoading -> CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 1.5.dp,
                        color = onSurfaceVariant,
                    )
                    state.isResendEnabled -> {
                        val resendText = buildAnnotatedString {
                            withLink(LinkAnnotation.Clickable(tag = "RESEND") { onResendClick() }) {
                                withStyle(
                                    baseStyle.copy(
                                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                                        color = MaterialTheme.colorScheme.primary,
                                        textDecoration = TextDecoration.None,
                                    )
                                ) { append(stringResource(CoreR.string.confirm_sign_up_resend)) }
                            }
                        }
                        Text(text = resendText)
                    }
                    else -> Text(
                        text = stringResource(CoreR.string.confirm_sign_up_resend_cooldown, state.formattedCooldown),
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        fontSize = 14.sp,
                        color = disabled,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmSignUpContentPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        ConfirmSignUpContent(
            state = ConfirmSignUpState(
                email = "user@example.com", resendCooldownSeconds = 0,
                isResendLoading = true
            ),
            onBackClick = {},
            onCodeChange = {},
            onConfirmClick = {},
            onResendClick = {},
        )
    }
}
