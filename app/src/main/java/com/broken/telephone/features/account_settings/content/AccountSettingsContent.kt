package com.broken.telephone.features.account_settings.content

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.account_settings.model.AccountSettingsState
import com.broken.telephone.features.edit_profile.content.AccountTextInfoItem
import com.broken.telephone.features.edit_profile.content.EditProfileTopBar
import com.broken.telephone.features.settings.content.SettingsLogoutButton

@Composable
fun AccountSettingsContent(
    state: AccountSettingsState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onBlockedUsersClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = "Account Settings",
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        AccountTextInfoItem(
            name = "Blocked Users",
            value = state.blockedUsersCount.toString(),
            modifier = Modifier
                .clickable(onClick = onBlockedUsersClick)
                .padding(horizontal = 16.dp),
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        SettingsLogoutButton(
            text = "Delete Account",
            onClick = onDeleteAccountClick,
        )

    }

}

@Preview
@Composable
fun AccountSettingsContentPreview() {
    BrokenTelephoneTheme() {
        AccountSettingsContent(state = AccountSettingsState(blockedUsersCount = 3))
    }
}