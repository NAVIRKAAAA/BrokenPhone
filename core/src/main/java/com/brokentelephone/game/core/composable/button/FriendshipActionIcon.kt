package com.brokentelephone.game.core.composable.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.model.friend.FriendshipActionState

@Composable
fun FriendshipActionIcon(
    state: FriendshipActionState?,
    modifier: Modifier = Modifier,
    onAddFriendClick: () -> Unit = {},
    onCancelRequestClick: () -> Unit = {},
    onRemoveFriendClick: () -> Unit = {},
    onAcceptRequestClick: () -> Unit = {},
    isLoading: Boolean = false,
) {
    if (isLoading) {
        Box(
            modifier = modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                strokeWidth = 2.dp,
            )
        }
        return
    }

    when (state) {
        FriendshipActionState.NOT_FRIENDS -> IconButton(
            onClick = onAddFriendClick,
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_person_add),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        FriendshipActionState.INVITE_SENT -> IconButton(
            onClick = onCancelRequestClick,
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_person_pending),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        FriendshipActionState.FRIENDS -> IconButton(
            onClick = onRemoveFriendClick,
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        
        FriendshipActionState.INVITE_RECEIVED -> IconButton(
            onClick = onAcceptRequestClick,
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }


        else -> return
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendshipActionIconPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendshipActionIcon(
                state = FriendshipActionState.INVITE_RECEIVED,
                onAddFriendClick = {},
                onCancelRequestClick = {},
                onRemoveFriendClick = {},
                modifier = Modifier.align(Alignment.Center),
                isLoading = false
            )
        }
    }
}
