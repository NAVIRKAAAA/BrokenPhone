package com.brokentelephone.game.features.notifications.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
fun ChainNotificationItem(
    item: NotificationUi.ChainInfo,
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(8.dp),
        ) {
            if (!isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_mutations),
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

private val previewUnread = NotificationUi.ChainInfo(
    id = "1",
    createdAt = System.currentTimeMillis() - 10 * 60 * 1000L,
    chainId = "c1",
    postId = "p1",
    title = "Your chain is complete!",
    body = "Someone finished the chain you started. Tap to see the result.",
)

private val previewRead = NotificationUi.ChainInfo(
    id = "2",
    createdAt = System.currentTimeMillis() - 3 * 60 * 60 * 1000L,
    chainId = "c2",
    postId = "p2",
    title = "New step in your chain",
    body = "A player added a drawing to your chain.",
)

@Preview(showBackground = true)
@Composable
private fun ChainNotificationItemUnreadPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()) {
            ChainNotificationItem(item = previewUnread, isRead = true, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChainNotificationItemReadPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()) {
            ChainNotificationItem(item = previewRead, isRead = false, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChainNotificationItemLightPreview() {
    BrokenTelephoneTheme(darkTheme = false) {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()) {
            ChainNotificationItem(item = previewUnread, isRead = false, onClick = {})
        }
    }
}
