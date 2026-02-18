package com.broken.telephone.features.create_post.dialog.chain_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R

@Composable
fun ChainSettingsItem(
    title: String,
    value: String,
    range: String,
    modifier: Modifier = Modifier,
    onMinusClick: () -> Unit = {},
    onPlusClick: () -> Unit = {},
    isMinusEnabled: Boolean = true,
    isPlusEnabled: Boolean = true,
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChainSettingsButton(
                type = ChainSettingsButtonType.MINUS,
                enabled = isMinusEnabled,
                onClick = onMinusClick
            )

            Box(
                modifier = Modifier.padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            ChainSettingsButton(
                type = ChainSettingsButtonType.PLUS,
                enabled = isPlusEnabled,
                onClick = onPlusClick
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = range,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 10.sp,
            lineHeight = 15.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Preview
@Composable
fun ChainSettingsItemPreview() {
    ChainSettingsItem(
        title = "Chain length",
        value = "10",
        range = "5-20"
    )
}