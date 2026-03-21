package com.brokentelephone.game.features.blocked_users.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.brokentelephone.game.core.avatar.AvatarComponent
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberRelativeTime
import com.brokentelephone.game.features.blocked_users.model.BlockedUserUi

@Composable
fun BlockedUserItem(
    blockedUserUi: BlockedUserUi,
    onUnblockClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val relativeTime = rememberRelativeTime(blockedUserUi.createdAt)

    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AvatarComponent(
                avatarUrl = blockedUserUi.avatarUrl,
                size = 40.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = blockedUserUi.name,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(4.dp))


                Text(
                    text = stringResource(R.string.blocked_users_item_blocked_on, relativeTime),
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onUnblockClick,
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 0.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.blocked_users_item_unblock),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                )
            }
        }

    }

}

@Preview
@Composable
fun BlockedUserItemPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            BlockedUserItem(
                blockedUserUi = BlockedUserUi(
                    id = "",
                    name = "Alex",
                    avatarUrl = null,
                    createdAt = System.currentTimeMillis(),
                ),
                onUnblockClick = {},
            )
        }
    }
}