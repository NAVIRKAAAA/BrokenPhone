package com.brokentelephone.game.features.notifications.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.model.notification.NotificationUi
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberRelativeTime

@Composable
fun NewsNotificationItem(
    item: NotificationUi.News,
    isRead: Boolean,
    onClick: () -> Unit,
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (!isRead) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(8.dp),
            ) {

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                )
            }


            Spacer(modifier = Modifier.width(10.dp))
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_megaphone),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.body,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = timestamp,
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private val previewUnread = NotificationUi.News(
    id = "1",
    title = "New feature dropped",
    body = "You can now use 3 new brush types in the drawing screen.",
    createdAt = System.currentTimeMillis() - 2 * 60 * 60 * 1000L,
)

private val previewRead = NotificationUi.News(
    id = "2",
    title = "App update available",
    body = "Version 2.1 brings performance improvements and bug fixes.",
    createdAt = System.currentTimeMillis() - 24 * 60 * 60 * 1000L,
)

private val previewLongBody = NotificationUi.News(
    id = "3",
    title = "Big announcement",
    body = "We have some exciting news to share with the community. Stay tuned for more details coming very soon!",
    createdAt = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000L,
)

@Preview(showBackground = true)
@Composable
private fun NewsNotificationItemUnreadPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NewsNotificationItem(item = previewUnread, isRead = true, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsNotificationItemReadPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {

            NewsNotificationItem(item = previewRead, isRead = false, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsNotificationItemLongBodyPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {

            NewsNotificationItem(item = previewLongBody, isRead = false, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsNotificationItemLightPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            NewsNotificationItem(item = previewUnread, isRead = false, onClick = {})
        }
    }
}
