package com.broken.telephone.features.profile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.broken.telephone.R

@Composable
fun AccountInfoSection(
    username: String,
    modifier: Modifier = Modifier,
    postsCount: Int = 0,
    contributions: Int = 0,
    avatarUrl: String? = null,
) {

    Row(
        modifier = modifier.fillMaxWidth()
    ) {

        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
        ) {
            Text(
                text = username,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.inter_regular)),
                fontSize = 19.sp,
                lineHeight = 28.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                StatInfo(
                    value = postsCount,
                    name = stringResource(R.string.profile_posts)
                )

                StatInfo(
                    value = contributions,
                    name = stringResource(R.string.profile_contributions)
                )
            }
        }
    }

}

@Preview
@Composable
fun AccountInfoSectionPreview() {
    AccountInfoSection(
        username = "Alex"
    )
}