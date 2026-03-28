package com.brokentelephone.game.features.notifications.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.core.top_bar.EditProfileTopBar
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.features.edit_profile.content.AccountTextInfoItem
import com.brokentelephone.game.features.notifications.model.NotificationsState

@Composable
fun NotificationsContent(
    state: NotificationsState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNotificationPermissionClick: () -> Unit = {},
    onAllNotificationsToggle: (Boolean) -> Unit = {},
    onNotificationToggle: (NotificationType, Boolean) -> Unit = { _, _ -> },
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = stringResource(R.string.notifications_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        AccountTextInfoItem(
            name = stringResource(R.string.notifications_permission),
            value = if (state.isNotificationPermissionGranted) stringResource(R.string.notifications_permission_on) else stringResource(R.string.notifications_permission_off),
            modifier = Modifier
                .clickable(onClick = onNotificationPermissionClick)
                .padding(horizontal = 16.dp),
        )

        HorizontalDivider(color = MaterialTheme.appColors.divider)

        Spacer(modifier = Modifier.height(8.dp))

        NotificationSwitchItem(
            text = stringResource(R.string.notifications_all),
            checked = NotificationType.entries.all { it in state.notifications },
            onCheckedChange = onAllNotificationsToggle,
        )

        NotificationType.entries.forEach { type ->
            NotificationSwitchItem(
                text = stringResource(type.displayNameResId),
                checked = type in state.notifications,
                onCheckedChange = { enabled -> onNotificationToggle(type, enabled) },
            )
        }
    }
}

@Preview
@Composable
fun NotificationsContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        NotificationsContent(state = NotificationsState())
    }
}
