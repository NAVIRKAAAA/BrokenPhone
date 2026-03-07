package com.broken.telephone.core.bottom_sheet.post_bottom_sheet.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme

@Composable
fun PostBottomSheetButton(
    text: String,
    iconResId: Int,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Unspecified,
    iconColor: Color = Color.Unspecified,
) {
    val resolvedTextColor = if (textColor == Color.Unspecified) MaterialTheme.colorScheme.onSurface else textColor
    val resolvedIconColor = if (iconColor == Color.Unspecified) MaterialTheme.colorScheme.onSurfaceVariant else iconColor

    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = resolvedIconColor
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = resolvedTextColor
        )

    }

}

@Preview
@Composable
fun PostBottomSheetButtonPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            PostBottomSheetButton(
                text = "Not Interested",
                iconResId = R.drawable.ic_not_interested,
            )
        }
    }
}