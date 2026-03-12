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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R

@Composable
fun SignUpTextFieldValue(
    value: TextFieldValue,
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit = {},
    label: String = "",
    error: String? = null,
    hint: String? = null,
    maxLength: Int? = null,
    onClearClick: (() -> Unit)? = null,
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
                            text = "${value.text.length}/$maxLength",
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = if (value.text.length > maxLength) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
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
        value = value,
        onValueChange = onValueChange,
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
        trailingIcon = when {
            onPasswordVisibilityToggle != null -> {
                {
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Icon(
                            painter = painterResource(
                                if (isPasswordVisible) R.drawable.password_visibility_off
                                else R.drawable.password_visibility_on
                            ),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            onClearClick != null -> {
                {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            else -> null
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
