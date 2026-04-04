package com.brokentelephone.game.features.post_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.modifier.hidden
import com.brokentelephone.game.core.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.features.post_details.model.PostDetailsButtonType

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
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                )
            }
        }
    }
}

@Composable
fun PostDetailsButtonShimmer(
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {},
        modifier = modifier
            .fillMaxWidth(0.6f)
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .shimmer(cornerRadius = 32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        enabled = false,
        shape = RoundedCornerShape(32.dp),
        contentPadding = PaddingValues(),
    ) {
        Text(
            text = "Continue",
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            modifier = Modifier.hidden()
        )
    }
}

@Preview
@Composable
fun PostDetailsButtonShimmerPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            PostDetailsButtonShimmer()
        }
    }
}
