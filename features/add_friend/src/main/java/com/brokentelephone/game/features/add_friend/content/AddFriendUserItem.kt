package com.brokentelephone.game.features.add_friend.content

import androidx.compose.foundation.background
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
import com.brokentelephone.game.core.avatar.AvatarComponent
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.profile.FriendshipActionIcon
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.utils.rememberMemberSince
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.features.add_friend.model.AddFriendUserUi

@Composable
fun AddFriendUserItem(
    item: AddFriendUserUi,
    onAddFriendClick: () -> Unit,
    onCancelRequestClick: () -> Unit,
    onRemoveFriendClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val memberSince = rememberMemberSince(item.user.createdAt)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AvatarComponent(
            avatarUrl = item.user.avatarUrl,
            size = 40.dp,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.user.username,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = stringResource(R.string.profile_member_since, memberSince),
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        FriendshipActionIcon(
            state = item.friendshipState,
            onAddFriendClick = onAddFriendClick,
            onCancelRequestClick = onCancelRequestClick,
            onRemoveFriendClick = onRemoveFriendClick,
            isLoading = isLoading,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddFriendUserItemPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            AddFriendUserItem(
                item = AddFriendUserUi(
                    user = UserUi(
                        id = "1",
                        username = "alex_doe",
                        email = "",
                        avatarUrl = null,
                        createdAt = 1740000000000L,
                        authProvider = AuthProvider.EMAIL,
                    ),
                    friendshipState = FriendshipActionState.INVITE_SENT,
                ),
                onAddFriendClick = {},
                onCancelRequestClick = {},
                onRemoveFriendClick = {},
            )
        }
    }
}
