package com.brokentelephone.game.core.composable.text_field

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.ext.modifier.coloredShadow
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

val SearchTextFieldHeight = 40.dp

@Composable
fun SearchTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onClearClick: () -> Unit = {},
    onImeAction: () -> Unit = {},
) {
    val textStyle = TextStyle(
        fontFamily = FontFamily(Font(R.font.nunito_regular)),
        fontSize = 15.sp,
        lineHeight = 22.sp,
        color = MaterialTheme.colorScheme.onSurface,
    )
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = textStyle,
        singleLine = true,
        maxLines = 1,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onImeAction() }),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .height(SearchTextFieldHeight)
                    .coloredShadow(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.14f),
                        blurRadius = 16f,
                        offsetY = 0.dp,
                        offsetX = 0.dp,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp),
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    innerTextField()
                }
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = onClearClick,
                        modifier = Modifier.size(20.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.search_text_field_clear),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchTextFieldEmptyPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        SearchTextField(
            text = "",
            onTextChange = {},
            placeholder = "Search friends",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchTextFieldWithTextPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        SearchTextField(
            text = "Alex",
            onTextChange = {},
            placeholder = "Search friends",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchTextFieldLightPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        SearchTextField(
            text = "",
            onTextChange = {},
            placeholder = "Search friends",
        )
    }
}
