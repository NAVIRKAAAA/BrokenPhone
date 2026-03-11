package com.brokentelephone.game.features.sign_up.content

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
import com.brokentelephone.game.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun SignUpTextField(
    text: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit = {},
    label: String = "",
    error: String? = null,
    hint: String? = null,
    maxLength: Int? = null,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {},
) {
    val supportingText: @Composable (() -> Unit)? = when {
        (error != null && error.isNotBlank()) || hint != null || maxLength != null -> {
            {
                Row(modifier = Modifier.fillMaxWidth()) {
                    when {
                        error != null -> Text(
                            text = error,
                            modifier = Modifier.weight(1f),
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                        )
                        hint != null -> Text(
                            text = hint,
                            modifier = Modifier.weight(1f),
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        else -> Spacer(modifier = Modifier.weight(1f))
                    }
                    if (maxLength != null) {
                        Text(
                            text = "${text.length}/$maxLength",
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = if (text.length > maxLength) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        else -> null
    }

    val visualTransformation = when {
        onPasswordVisibilityToggle == null -> VisualTransformation.None
        isPasswordVisible -> VisualTransformation.None
        else -> PasswordVisualTransformation()
    }

    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 15.sp,
            lineHeight = 22.sp
        ),
        minLines = 1,
        maxLines = 1,
        singleLine = true,
        label = {
            Text(
                text = label,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        supportingText = supportingText,
        isError = error != null,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        trailingIcon = onPasswordVisibilityToggle?.let {
            {
                IconButton(onClick = it) {
                    Icon(
                        painter = painterResource(
                            if (isPasswordVisible) R.drawable.password_visibility_off
                            else R.drawable.password_visibility_on
                        ),
                        contentDescription = if (isPasswordVisible) stringResource(R.string.sign_up_password_hide) else stringResource(R.string.sign_up_password_show),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error
        ),
        shape = RoundedCornerShape(14.dp),
    )
}

@Preview
@Composable
fun SignUpTextFieldPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        SignUpTextField(
            text = "",
            label = "Email",
            error = "Email is required"
        )
    }
}

@Preview
@Composable
fun SignUpTextFieldPasswordPreview() {
    var visible by remember { mutableStateOf(false) }
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        SignUpTextField(
            text = "secret123".repeat(555),
            label = "Password",
            isPasswordVisible = visible,
            onPasswordVisibilityToggle = { visible = !visible },
            error = "Lalala",
            maxLength = 140
        )
    }
}