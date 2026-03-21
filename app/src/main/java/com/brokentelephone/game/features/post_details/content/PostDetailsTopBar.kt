package com.brokentelephone.game.features.post_details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.shimmer.shimmer
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun PostDetailsTopBar(
    onBackClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(
            onClick = onBackClick
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )

        }

        Text(
            text = stringResource(R.string.post_details_title),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            fontSize = 18.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )

        IconButton(
            onClick = onMoreClick,
            enabled = !isLoading,
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_more_vert),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = if (isLoading) Modifier.shimmer(cornerRadius = 4.dp) else Modifier,
            )

        }

    }

}

@Preview
@Composable
fun PostDetailsTopBarPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            PostDetailsTopBar()
        }
    }
}