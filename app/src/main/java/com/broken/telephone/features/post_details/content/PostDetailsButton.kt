package com.broken.telephone.features.post_details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.appColors
import com.broken.telephone.features.post_details.model.PostDetailsButtonType

@Composable
fun PostDetailsButton(
    buttonType: PostDetailsButtonType,
    isLoading: Boolean,
    onContinueClick: () -> Unit,
    onViewHistoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (buttonType == PostDetailsButtonType.COMPLETED) {
        MaterialTheme.appColors.badgeComplete
    } else {
        MaterialTheme.colorScheme.primary
    }

    Button(
        onClick = if (buttonType == PostDetailsButtonType.COMPLETED) onViewHistoryClick else onContinueClick,
        modifier = modifier
            .fillMaxWidth(0.6f)
            .height(48.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = containerColor,
        ),
        enabled = buttonType.isEnabled && !isLoading,
        shape = RoundedCornerShape(32.dp),
        contentPadding = PaddingValues(),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = LocalContentColor.current,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp),
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(buttonType.buttonTextResId),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.inter_medium)),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                )
            }
        }
    }
}
