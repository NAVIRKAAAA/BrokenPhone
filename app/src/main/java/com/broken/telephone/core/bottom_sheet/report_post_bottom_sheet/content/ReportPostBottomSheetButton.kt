package com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun ReportPostBottomSheetButton(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
) {

    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = textColor
        )

    }

}

@Preview
@Composable
fun ReportPostBottomSheetButtonPreview() {
    BrokenTelephoneTheme {
        ReportPostBottomSheetButton(text = "Spam")
    }
}
