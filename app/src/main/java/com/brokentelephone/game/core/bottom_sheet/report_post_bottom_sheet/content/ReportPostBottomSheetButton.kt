package com.brokentelephone.game.core.bottom_sheet.report_post_bottom_sheet.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun ReportPostBottomSheetButton(
    text: String,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface
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
