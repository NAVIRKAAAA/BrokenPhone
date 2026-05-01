package com.brokentelephone.game.core.composable.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.avatar.AvatarComponent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun AccountInfoSection(
    username: String,
    modifier: Modifier = Modifier,
    postsCount: Int = 0,
    contributions: Int = 0,
    friends: Int = 0,
    avatarUrl: String? = null,
    bio: String = "",
    createdAt: Long? = null,
    onFriendsClick: () -> Unit = {},
    onPostsClick: () -> Unit = {},
    onContributionsClick: () -> Unit = {},
) {
    val avatarSize = 82.dp

    Column(modifier = modifier.fillMaxWidth()) {

        Row {
            AvatarComponent(
                avatarUrl = avatarUrl,
                size = avatarSize
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .height(avatarSize),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = username,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                    fontSize = 19.sp,
                    lineHeight = 28.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    StatInfo(
                        value = postsCount,
                        name = stringResource(R.string.profile_posts),
                        enabled = true,
                        onClick = onPostsClick,
                    )
                    StatInfo(
                        value = contributions,
                        name = stringResource(R.string.profile_contributions),
                        enabled = true,
                        onClick = onContributionsClick,
                    )
                    StatInfo(
                        value = friends,
                        name = stringResource(R.string.profile_friends),
                        enabled = true,
                        onClick = onFriendsClick,
                    )
                }

            }
        }

        if (createdAt != null) {
            UserBioDisplay(
                bio = bio,
                createdAt = createdAt,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AccountInfoSectionPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            AccountInfoSection(
                username = "Alex",
                bio = "I love drawing, creative games, and exploring new ideas. Always up for a challenge and meeting new people through fun activities!",
                createdAt = System.currentTimeMillis()
            )
        }
    }
}
