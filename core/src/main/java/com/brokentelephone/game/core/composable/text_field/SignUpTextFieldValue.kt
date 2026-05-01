package com.brokentelephone.game.core.composable.text_field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R

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
    maxLines: Int = 1,
    minLines: Int = 1,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    onImeAction: () -> Unit = {},
) {
    val text = value.text
    val interactionSource = remember { MutableInteractionSource() }

    val primaryTextSize = 18.sp
    val primaryTextLineHeight = 24.sp

    val labelTextSize = 14.sp
    val supportingTextSize = 14.sp

    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

    val supportingText: @Composable (() -> Unit)? = when {
        !error.isNullOrBlank() || hint != null || maxLength != null -> {
            {
                Row(modifier = Modifier.fillMaxWidth()) {
                    when {
                        error != null -> Text(
                            text = error,
                            modifier = Modifier.weight(1f),
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = supportingTextSize,
                        )

                        hint != null -> Text(
                            text = hint,
                            modifier = Modifier.weight(1f),
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = supportingTextSize,
                            color = labelColor,
                        )

                        else -> Spacer(modifier = Modifier.weight(1f))
                    }
                    if (maxLength != null) {
                        Text(
                            text = "${text.length}/$maxLength",
                            fontFamily = FontFamily(Font(R.font.nunito_regular)),
                            fontSize = supportingTextSize,
                            color = if (text.length > maxLength) MaterialTheme.colorScheme.error else labelColor,
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

    val colors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        errorBorderColor = MaterialTheme.colorScheme.error,
        errorSupportingTextColor = MaterialTheme.colorScheme.error,
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = primaryTextSize,
            lineHeight = primaryTextLineHeight,
            color = MaterialTheme.colorScheme.onSurface,
        ),
        singleLine = true,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        interactionSource = interactionSource,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value.text,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                isError = error != null,
                label = {
                    Text(
                        text = label,
                        fontFamily = FontFamily(Font(R.font.nunito_regular)),
                        fontSize = labelTextSize,
                        color = labelColor,
                    )
                },
                trailingIcon = when {
                    onPasswordVisibilityToggle != null -> {
                        {
                            IconButton(onClick = onPasswordVisibilityToggle) {
                                Icon(
                                    painter = painterResource(
                                        if (isPasswordVisible) R.drawable.password_visibility_on
                                        else R.drawable.password_visibility_off
                                    ),
                                    contentDescription = if (isPasswordVisible) stringResource(R.string.sign_up_password_hide) else stringResource(
                                        R.string.sign_up_password_show
                                    ),
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
                supportingText = supportingText,
                colors = colors,
                contentPadding = PaddingValues(16.dp),
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = true,
                        isError = error != null,
                        interactionSource = interactionSource,
                        colors = colors,
                        shape = RoundedCornerShape(16.dp),
                    )
                },
            )
        },
    )
}
