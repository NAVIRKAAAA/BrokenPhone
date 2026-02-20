package com.broken.telephone.core.badge

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R

@Composable
fun BadgeElement(
    iconResId: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .clickable(onClick = onClick, enabled = enabled)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Color.Gray
        )

    }

}

@Composable
fun BadgeElement(
    iconResId: Int,
    text: String,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Color.Gray
        )

    }

}

@Preview
@Composable
fun BadgeElementPreview() {
    BadgeElement(
        R.drawable.ic_mutations,
        text = "0/10",
        onClick = {},
    )
}