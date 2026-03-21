package com.brokentelephone.game.core.top_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun SaveTopBar(
    title: String,
    saveButtonText: String,
    modifier: Modifier = Modifier,
    isSaveEnabled: Boolean = true,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 18.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.primary,
        )

        TextButton(
            onClick = onSaveClick,
            enabled = isSaveEnabled,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = LocalContentColor.current,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = saveButtonText,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun SaveTopBarPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box {
            SaveTopBar(
                title = "Edit Avatar",
                saveButtonText = "Save",
                isSaveEnabled = false,
                isLoading = true
            )
        }
    }
}
