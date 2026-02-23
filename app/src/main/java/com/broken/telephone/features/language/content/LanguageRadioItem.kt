package com.broken.telephone.features.language.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme

@Composable
fun LanguageRadioItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    body: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors().copy(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = text,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 15.sp,
                lineHeight = 22.sp,
            )

            if (body != null) {

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = body,
                    fontFamily = FontFamily(Font(R.font.inter_regular)),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = Color(0xFF999999),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageRadioItemPreview() {
    BrokenTelephoneTheme {
        LanguageRadioItem(
            text = "System",
            body = "Match device settings",
            selected = true,
            onClick = {},
        )
    }
}
