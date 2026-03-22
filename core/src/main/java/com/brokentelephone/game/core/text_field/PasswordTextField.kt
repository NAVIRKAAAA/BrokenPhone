package com.brokentelephone.game.core.text_field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun PasswordTextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    modifier: Modifier = Modifier,
    onForgotPasswordClick: () -> Unit,
    error: String? = null,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {},
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 15.sp,
            lineHeight = 22.sp,
        ),
        singleLine = true,
        label = {
            Text(
                text = label,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (error != null && error.isNotBlank()) {
                    Text(
                        text = error,
                        modifier = Modifier.weight(1f),
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Text(
                    text = stringResource(R.string.sign_in_forgot_password),
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(
                        onClick = onForgotPasswordClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                )
            }
        },
        isError = error != null,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityToggle) {
                Icon(
                    painter = painterResource(
                        if (isPasswordVisible) R.drawable.password_visibility_off
                        else R.drawable.password_visibility_on
                    ),
                    contentDescription = if (isPasswordVisible) {
                        stringResource(R.string.sign_up_password_hide)
                    } else {
                        stringResource(R.string.sign_up_password_show)
                    },
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
        ),
        shape = RoundedCornerShape(14.dp),
    )
}

@Preview
@Composable
private fun PasswordTextFieldPreview() {
    var visible by remember { mutableStateOf(false) }
    BrokenTelephoneTheme(darkTheme = true) {
        PasswordTextField(
            text = "secret123",
            onTextChange = {},
            label = "Password",
            isPasswordVisible = visible,
            onPasswordVisibilityToggle = { visible = !visible },
            onForgotPasswordClick = {},
        )
    }
}

@Preview
@Composable
private fun PasswordTextFieldErrorPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        PasswordTextField(
            text = "secret123",
            onTextChange = {},
            label = "Password",
            isPasswordVisible = false,
            onPasswordVisibilityToggle = {},
            onForgotPasswordClick = {},
            error = "Invalid password".repeat(3),
        )
    }
}

