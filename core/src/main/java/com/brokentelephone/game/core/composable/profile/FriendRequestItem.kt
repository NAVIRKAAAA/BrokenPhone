package com.brokentelephone.game.core.composable.profile

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.avatar.AvatarComponent
import com.brokentelephone.game.core.composable.button.BTButton
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun FriendRequestItem(
    user: UserUi,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier,
    isAcceptLoading: Boolean = false,
    isDeclineLoading: Boolean = false,
    enabled: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarComponent(
                avatarUrl = user.avatarUrl,
                size = 40.dp,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.username,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(4.dp))

                UserBioDisplayAsSingleLine(
                    bio = user.bio,
                    createdAt = user.createdAt
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 52.dp)
        ) {
            BTButton(
                text = stringResource(R.string.friendship_action_decline),
                onClick = onDeclineClick,
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                isLoading = isDeclineLoading,
                enabled = enabled,
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            BTButton(
                text = stringResource(R.string.friendship_action_accept),
                onClick = onAcceptClick,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                isLoading = isAcceptLoading,
                enabled = enabled,
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendRequestItemDarkPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendRequestItem(
                user = UserUi(
                    id = "1",
                    username = "alex_doe",
                    email = "",
                    avatarUrl = null,
                    createdAt = 1740000000000L,
//                    authProvider = AuthProvider.EMAIL,
                ),
                onAcceptClick = {},
                onDeclineClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendRequestItemLightPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendRequestItem(
                user = UserUi(
                    id = "1",
                    username = "very_long_username_that_overflows_the_layout",
                    email = "",
                    avatarUrl = null,
                    createdAt = 1740000000000L,
//                    authProvider = AuthProvider.EMAIL,
                ),
                onAcceptClick = {},
                onDeclineClick = {},
            )
        }
    }
}
