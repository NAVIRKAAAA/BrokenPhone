package com.brokentelephone.game.core.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.button.WelcomeButton
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.model.friend.FriendshipActionState

@Composable
fun FriendshipActionButton(
    state: FriendshipActionState?,
    onAddFriendClick: () -> Unit,
    onCancelRequestClick: () -> Unit,
    onRemoveFriendClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    when (state) {
        FriendshipActionState.IS_ME -> WelcomeButton(
            text = stringResource(R.string.friendship_action_is_me),
            onClick = {},
            enabled = false,
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = modifier.height(48.dp),
        )

        FriendshipActionState.NOT_FRIENDS -> WelcomeButton(
            text = stringResource(R.string.friendship_action_add),
            onClick = onAddFriendClick,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            isLoading = isLoading,
            modifier = modifier.height(48.dp),
        )

        FriendshipActionState.INVITE_RECEIVED -> WelcomeButton(
            text = stringResource(R.string.friendship_action_accept),
            onClick = onAddFriendClick,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            isLoading = isLoading,
            modifier = modifier.height(48.dp),
        )

        FriendshipActionState.INVITE_SENT -> WelcomeButton(
            text = stringResource(R.string.friendship_action_invite_sent),
            onClick = onCancelRequestClick,
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            isLoading = isLoading,
            modifier = modifier.height(48.dp),
        )

        FriendshipActionState.FRIENDS -> WelcomeButton(
            text = stringResource(R.string.friendship_action_friends),
            onClick = onRemoveFriendClick,
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            isLoading = isLoading,
            modifier = modifier.height(48.dp),
        )

        else -> WelcomeButton(
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
                state = FriendshipActionState.NOT_FRIENDS,
                onAddFriendClick = {},
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
                state = FriendshipActionState.INVITE_SENT,
                onAddFriendClick = {},
                onCancelRequestClick = {},
                onRemoveFriendClick = {},
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
            )
        }
    }
}
