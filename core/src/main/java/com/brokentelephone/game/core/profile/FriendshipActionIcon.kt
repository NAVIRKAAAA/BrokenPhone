package com.brokentelephone.game.core.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.domain.model.friend.FriendshipActionState

@Composable
fun FriendshipActionIcon(
    state: FriendshipActionState?,
    onAddFriendClick: () -> Unit,
    onCancelRequestClick: () -> Unit,
    onRemoveFriendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

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
                painter = painterResource(R.drawable.ic_person_remove),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        else -> {
            IconButton(
                enabled = false,
                onClick = {return@IconButton}
            ) {
                CircularProgressIndicator(
                    modifier = modifier,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FriendshipActionIconPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            FriendshipActionIcon(
                state = null,
                onAddFriendClick = {},
                onCancelRequestClick = {},
                onRemoveFriendClick = {},
                modifier = Modifier.align(Alignment.Center),

            )
        }
    }
}
