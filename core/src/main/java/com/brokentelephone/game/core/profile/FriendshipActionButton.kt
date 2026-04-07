package com.brokentelephone.game.core.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.button.BTButton
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.domain.model.friend.FriendshipActionState

@Composable
fun FriendshipActionButton(
    state: FriendshipActionState?,
    onAddFriendClick: () -> Unit,
    onAcceptRequestClick: () -> Unit,
    onCancelRequestClick: () -> Unit,
    onRemoveFriendClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    when (state) {
        FriendshipActionState.IS_ME -> BTButton(
            text = stringResource(R.string.friendship_action_add),
            onClick = {},
            enabled = false,
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = modifier.height(48.dp),
        )

        FriendshipActionState.NOT_FRIENDS -> BTButton(
            text = stringResource(R.string.friendship_action_add),
            onClick = onAddFriendClick,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            isLoading = isLoading,
            modifier = modifier.height(48.dp),
        )

        FriendshipActionState.INVITE_RECEIVED -> {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                BTButton(
                    text = stringResource(R.string.friendship_action_decline),
                    onClick = {},
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    isLoading = isLoading,
                    modifier = modifier.weight(1f).height(48.dp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                BTButton(
                    text = stringResource(R.string.friendship_action_accept),
                    onClick = onAcceptRequestClick,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    isLoading = isLoading,
                    modifier = modifier.weight(1f).height(48.dp),
                )
            }
        }

        FriendshipActionState.INVITE_SENT -> BTButton(
            text = stringResource(R.string.friendship_action_invite_sent),
            onClick = onCancelRequestClick,
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            isLoading = isLoading,
            modifier = modifier.height(48.dp),
        )

        FriendshipActionState.FRIENDS -> BTButton(
            text = stringResource(R.string.friendship_action_friends),
            onClick = onRemoveFriendClick,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.appColors.badgeComplete,
            isLoading = isLoading,
            modifier = modifier.height(48.dp),
        )

        else -> BTButton(
            text = "",
            onClick = {},
            isLoading = true,
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = modifier.height(48.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendshipActionButtonNotFriendsPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendshipActionButton(
                state = FriendshipActionState.INVITE_RECEIVED,
                onAddFriendClick = {},
                onAcceptRequestClick = {},
                onCancelRequestClick = {},
                onRemoveFriendClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendshipActionButtonInviteSentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendshipActionButton(
                state = FriendshipActionState.FRIENDS,
                onAddFriendClick = {},
                onCancelRequestClick = {},
                onRemoveFriendClick = {},
                onAcceptRequestClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendshipActionButtonFriendsPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendshipActionButton(
                state = null,
                onAddFriendClick = {},
                onCancelRequestClick = {},
                onRemoveFriendClick = {},
                onAcceptRequestClick = {}
            )
        }
    }
}
