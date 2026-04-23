package com.brokentelephone.game.features.account_settings.content

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
import com.brokentelephone.game.core.button.SettingsLogoutButton
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.profile.AccountStaticInfoItem
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.core.top_bar.EditProfileTopBar
import com.brokentelephone.game.core.utils.rememberMemberSince
import com.brokentelephone.game.features.account_settings.model.AccountSettingsState

@Composable
fun AccountSettingsContent(
    state: AccountSettingsState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onVerifyEmailClick: () -> Unit = {},
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

        val isEmailVerified = state.user?.isEmailVerified ?: false

        AccountEmailInfoItem(
            email = state.user?.email ?: "",
            isVerified = isEmailVerified,
            modifier = Modifier
                .clickable(onClick = onVerifyEmailClick, enabled = !isEmailVerified && !state.isVerifyEmailLoading)
                .padding(horizontal = 16.dp),
        )

//        AccountStaticInfoItem(
//            name = stringResource(R.string.account_settings_provider),
//            value = state.user?.authProvider?.let { stringResource(it.labelResId) },
//            modifier = Modifier.padding(horizontal = 16.dp),
//        )

        AccountStaticInfoItem(
            name = stringResource(R.string.account_settings_member_since),
            value = state.user?.createdAt?.let { rememberMemberSince(it) },
            modifier = Modifier.padding(horizontal = 16.dp),
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
//                    authProvider = AuthProvider.EMAIL,
                    createdAt = 1_700_000_000_000L,
                ),
            )
        )
    }
}