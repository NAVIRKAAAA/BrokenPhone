package com.brokentelephone.game.features.profile.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.avatar.AvatarComponent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun ProfileTopBar(
    title: String,
    username: String,
    avatarUrl: String?,
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isScrolled: Boolean,
    showEditButton: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        AnimatedContent(
            targetState = isScrolled,
            transitionSpec = {
                if (targetState) {
                    slideInVertically { -it } + fadeIn() togetherWith slideOutVertically { it } + fadeOut()
                } else {
                    slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
                }
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
        ) { scrolled ->
            if (scrolled) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(vertical = 8.dp),
                ) {
                    AvatarComponent(avatarUrl = avatarUrl, size = 40.dp)

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = username,
                        fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                        fontSize = 17.sp,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            } else {
                Text(
                    text = title,
                    fontFamily = FontFamily(Font(R.font.nunito_extra_bold)),
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.statusBarsPadding().padding(vertical = 8.dp),
                )
            }
        }

        Row(modifier = Modifier.statusBarsPadding().padding(vertical = 8.dp)) {
            if (showEditButton) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = stringResource(R.string.profile_top_bar_edit),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
            }

            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.profile_top_bar_settings),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileTopBarPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        ProfileTopBar(
            title = "Profile",
            username = "alex_username",
            avatarUrl = null,
            onEditClick = {},
            onSettingsClick = {},
            isScrolled = false,
        )
    }
}

@Preview
@Composable
private fun ProfileTopBarScrolledPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        ProfileTopBar(
            title = "Profile",
            username = "alex_username",
            avatarUrl = null,
            onEditClick = {},
            onSettingsClick = {},
            isScrolled = true,
        )
    }
}
