package com.broken.telephone.core.badge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R

enum class StrongBadgeElementType(
    val iconResId: Int,
    val text: String,
    val color: Color,
) {
    COMPLETE(
        iconResId = R.drawable.ic_complete,
        text = "Complete",
        color = Color(0xFF22C55E),
    )
}

@Composable
fun StrongBadgeElement(
    type: StrongBadgeElementType,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Icon(
            painter = painterResource(type.iconResId),
            contentDescription = null,
            tint = type.color,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = type.text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = type.color
        )

    }
}
