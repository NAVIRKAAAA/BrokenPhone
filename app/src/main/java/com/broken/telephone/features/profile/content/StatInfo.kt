package com.broken.telephone.features.profile.content

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.broken.telephone.R

@Composable
fun StatInfo(
    value: Int,
    name: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        Text(
            text = value.toString(),
            textAlign = TextAlign.Start,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 17.sp,
            lineHeight = 25.sp,
        )

        Text(
            text = name,
            textAlign = TextAlign.Start,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = Color(0xFF999999)
        )
    }

}

@Preview
@Composable
fun StatInfoPreview() {
    StatInfo(
        value = 12,
        name = "Posts"
    )
}