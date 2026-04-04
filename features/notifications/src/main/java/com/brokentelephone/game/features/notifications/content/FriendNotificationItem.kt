package com.brokentelephone.game.features.notifications.content

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
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
import com.brokentelephone.game.core.model.notification.NotificationUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberRelativeTime
import com.brokentelephone.game.domain.model.notification.NotificationData

@Composable
fun FriendNotificationItem(
    item: NotificationUi.Friends,
    onClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val timestamp = rememberRelativeTime(item.createdAt)

    Row(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            )
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
                        val bodyText = stringResource(item.type.bodyResId())
                        val bold = SpanStyle(fontFamily = FontFamily(Font(R.font.nunito_bold)))
                        val regular = SpanStyle(fontFamily = FontFamily(Font(R.font.nunito_regular)))
                        if (item.type.isActionFirst()) {
                            withStyle(regular) { append(bodyText) }
                            append(" ")
                            withStyle(bold) { append(item.username) }
                        } else {
                            withStyle(bold) { append(item.username) }
                            append(" ")
                            withStyle(regular) { append(bodyText) }
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

            if (item.type == NotificationData.FriendsType.INVITE_RECEIVED) {
                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    WelcomeButton(
                        text = stringResource(R.string.friendship_action_decline),
                        onClick = onDeclineClick,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    WelcomeButton(
                        text = stringResource(R.string.friendship_action_accept),
                        onClick = onAcceptClick,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp),
                    )
                }
            }
        }
    }
}

@StringRes
private fun NotificationData.FriendsType.bodyResId(): Int = when (this) {
    NotificationData.FriendsType.INVITE_RECEIVED -> R.string.notification_friend_request_body
    NotificationData.FriendsType.INVITE_ACCEPTED -> R.string.notification_friend_accepted_body
    NotificationData.FriendsType.INVITE_ACCEPTED_BY_ME -> R.string.notification_friend_accepted_by_me_body
    NotificationData.FriendsType.INVITE_DECLINED_BY_ME -> R.string.notification_friend_declined_by_me_body
}

private fun NotificationData.FriendsType.isActionFirst(): Boolean = when (this) {
    NotificationData.FriendsType.INVITE_ACCEPTED_BY_ME,
    NotificationData.FriendsType.INVITE_DECLINED_BY_ME -> true
    else -> false
}

private val previewUnread = NotificationUi.Friends(
    id = "1",
    createdAt = System.currentTimeMillis() - 5 * 60 * 1000L,
    isRead = false,
    requestId = "req_1",
    userId = "u1",
    username = "alex_doe",
    userAvatarUrl = null,
    type = NotificationData.FriendsType.INVITE_RECEIVED,
)

private val previewRead = NotificationUi.Friends(
    id = "2",
    createdAt = System.currentTimeMillis() - 2 * 60 * 60 * 1000L,
    isRead = true,
    requestId = "req_2",
    userId = "u2",
    username = "alexander_the_great",
    userAvatarUrl = null,
    type = NotificationData.FriendsType.INVITE_ACCEPTED,
)

private val previewAcceptedByMe = NotificationUi.Friends(
    id = "3",
    createdAt = System.currentTimeMillis() - 30 * 60 * 1000L,
    isRead = true,
    requestId = "req_3",
    userId = "u3",
    username = "maria_k",
    userAvatarUrl = null,
    type = NotificationData.FriendsType.INVITE_ACCEPTED_BY_ME,
)

private val previewDeclinedByMe = NotificationUi.Friends(
    id = "4",
    createdAt = System.currentTimeMillis() - 3 * 60 * 60 * 1000L,
    isRead = true,
    requestId = "req_4",
    userId = "u4",
    username = "john_doe",
    userAvatarUrl = null,
    type = NotificationData.FriendsType.INVITE_DECLINED_BY_ME,
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

@Preview(showBackground = true)
@Composable
private fun FriendNotificationItemAcceptedByMePreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()) {
            FriendNotificationItem(
                item = previewAcceptedByMe,
                onClick = {},
                onAcceptClick = {},
                onDeclineClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendNotificationItemDeclinedByMePreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()) {
            FriendNotificationItem(
                item = previewDeclinedByMe,
                onClick = {},
                onAcceptClick = {},
                onDeclineClick = {},
            )
        }
    }
}
