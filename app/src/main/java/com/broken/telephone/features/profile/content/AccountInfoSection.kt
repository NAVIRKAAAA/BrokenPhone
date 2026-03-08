package com.broken.telephone.features.profile.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.avatar.AvatarComponent
import com.broken.telephone.core.theme.BrokenTelephoneTheme

@Composable
fun AccountInfoSection(
    username: String,
    isAuth: Boolean = true,
    modifier: Modifier = Modifier,
    postsCount: Int = 0,
    contributions: Int = 0,
    avatarUrl: String? = null,
) {

    Row(
        modifier = modifier.fillMaxWidth()
    ) {

        AvatarComponent(
            avatarUrl = avatarUrl,
            size = 64.dp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
        ) {
            Text(
                text = username,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                fontSize = 19.sp,
                lineHeight = 28.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if(isAuth) {
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
            } else {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign up to save your posts and\njoin the fun!",
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

}

@Preview
@Composable
fun AccountInfoSectionPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            AccountInfoSection(
                username = "Alex",
                isAuth = false
            )
        }
    }
}