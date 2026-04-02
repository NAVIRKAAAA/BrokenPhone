package com.brokentelephone.game.features.notifications.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.avatar.AvatarComponent
import com.brokentelephone.game.core.button.WelcomeButton
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberRelativeTime
import com.brokentelephone.game.features.notifications.model.FriendNotificationType
import com.brokentelephone.game.features.notifications.model.NotificationUi

@Composable
fun FriendNotificationItem(
    item: NotificationUi.Friends,
    onClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val timestamp = rememberRelativeTime(item.timestamp)

    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 20.dp)
                .size(8.dp),
        ) {
            if (!item.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        AvatarComponent(avatarUrl = item.userAvatarUrl, size = 44.dp)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontFamily = FontFamily(Font(R.font.nunito_bold)))) {
                            append(item.username)
                        }
                        append(" ")
                        withStyle(SpanStyle(fontFamily = FontFamily(Font(R.font.nunito_regular)))) {
                            append(stringResource(item.type.textResId))
                        }
                    },
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = timestamp,
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (item.type == FriendNotificationType.INVITE_RECEIVED) {
                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    WelcomeButton(
                        text = stringResource(R.string.friendship_action_decline),
                        onClick = onDeclineClick,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    WelcomeButton(
                        text = stringResource(R.string.friendship_action_accept),
                        onClick = onAcceptClick,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                    )
                }
            }
        }
    }
}

private val previewUnread = NotificationUi.Friends(
    id = "1",
    timestamp = System.currentTimeMillis() - 5 * 60 * 1000L,
    isRead = false,
    userId = "u1",
    username = "alex_doe",
    userAvatarUrl = null,
    type = FriendNotificationType.INVITE_RECEIVED,
)

private val previewRead = NotificationUi.Friends(
    id = "2",
    timestamp = System.currentTimeMillis() - 2 * 60 * 60 * 1000L,
    isRead = true,
    userId = "u2",
    username = "alexander_the_great",
    userAvatarUrl = null,
    type = FriendNotificationType.INVITE_ACCEPTED,
)

@Preview(showBackground = true)
@Composable
private fun FriendNotificationItemUnreadPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()) {
            FriendNotificationItem(
                item = previewUnread,
                onClick = {},
                onAcceptClick = {},
                onDeclineClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendNotificationItemReadPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()) {
            FriendNotificationItem(
                item = previewRead,
                onClick = {},
                onAcceptClick = {},
                onDeclineClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendNotificationItemLightPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()) {
            FriendNotificationItem(
                item = previewUnread,
                onClick = {},
                onAcceptClick = {},
                onDeclineClick = {},
            )
        }
    }
}
