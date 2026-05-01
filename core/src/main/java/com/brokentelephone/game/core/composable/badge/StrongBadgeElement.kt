package com.brokentelephone.game.core.composable.badge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.model.badge.StrongBadgeElementType
import com.brokentelephone.game.core.theme.appColors

@Composable
fun StrongBadgeElement(
    type: StrongBadgeElementType,
    modifier: Modifier = Modifier,
) {
    val appColors = MaterialTheme.appColors
    val color = when (type) {
        StrongBadgeElementType.COMPLETE -> appColors.badgeComplete
        StrongBadgeElementType.YOU -> appColors.badgeYou
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Icon(
            painter = painterResource(type.iconResId),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = stringResource(type.textResId),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = color
        )

    }
}
