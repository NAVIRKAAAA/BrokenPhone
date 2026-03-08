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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.core.theme.appColors
import com.broken.telephone.core.utils.rememberMemberSince
import com.broken.telephone.domain.user.AuthProvider
import com.broken.telephone.features.account_settings.model.AccountSettingsState
import com.broken.telephone.features.edit_profile.content.AccountStaticInfoItem
import com.broken.telephone.features.edit_profile.content.AccountTextInfoItem
import com.broken.telephone.features.edit_profile.content.EditProfileTopBar
import com.broken.telephone.features.profile.model.UserUi
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
            title = stringResource(R.string.account_settings_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        AccountTextInfoItem(
            name = stringResource(R.string.account_settings_email),
            value = state.user?.email ?: "",
            modifier = Modifier.padding(horizontal = 16.dp),
            enabled = false
        )

        AccountStaticInfoItem(
            name = stringResource(R.string.account_settings_provider),
            value = state.user?.authProvider?.let { stringResource(it.labelResId) },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        AccountStaticInfoItem(
            name = stringResource(R.string.account_settings_member_since),
            value = state.user?.createdAt?.let { rememberMemberSince(it) },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        HorizontalDivider(color = MaterialTheme.appColors.divider)

        AccountTextInfoItem(
            name = stringResource(R.string.account_settings_blocked_users),
            value = state.blockedUsersCount.toString(),
            modifier = Modifier
                .clickable(onClick = onBlockedUsersClick)
                .padding(horizontal = 16.dp),
        )

        HorizontalDivider(color = MaterialTheme.appColors.divider)

        SettingsLogoutButton(
            text = stringResource(R.string.account_settings_delete_account),
            onClick = onDeleteAccountClick,
        )

    }

}

@Preview
@Composable
fun AccountSettingsContentPreview() {
    BrokenTelephoneTheme {
        AccountSettingsContent(
            state = AccountSettingsState(
                user = UserUi(
                    id = "1",
                    username = "Alex",
                    email = "alex@example.com",
                    avatarUrl = null,
                    authProvider = AuthProvider.EMAIL,
                    createdAt = 1_700_000_000_000L,
                ),
                blockedUsersCount = 3,
            )
        )
    }
}