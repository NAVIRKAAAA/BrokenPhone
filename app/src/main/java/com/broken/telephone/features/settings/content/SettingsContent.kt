package com.broken.telephone.features.settings.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.core.theme.appColors
import com.broken.telephone.features.edit_profile.content.AccountTextInfoItem
import com.broken.telephone.features.edit_profile.content.EditProfileTopBar
import com.broken.telephone.features.settings.model.SettingsState


@Composable
fun SettingsContent(
    state: SettingsState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onAccountSettingsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onThemeClick: () -> Unit = {},
    onTermsOfServiceClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditProfileTopBar(
            title = stringResource(R.string.settings_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.isAuth) {
            SettingsItem(
                text = stringResource(R.string.settings_item_account),
                onClick = onAccountSettingsClick,
                modifier = Modifier
            )

            HorizontalDivider(color = MaterialTheme.appColors.divider)
        }

        AccountTextInfoItem(
            name = stringResource(R.string.app_preferences_language),
            value = stringResource(state.language.displayNameResId),
            modifier = Modifier
                .clickable(onClick = onLanguageClick)
                .padding(horizontal = 16.dp),
        )

        AccountTextInfoItem(
            name = stringResource(R.string.app_preferences_theme),
            value = stringResource(state.theme.displayNameResId),
            modifier = Modifier
                .clickable(onClick = onThemeClick)
                .padding(horizontal = 16.dp),
        )

        HorizontalDivider(color = MaterialTheme.appColors.divider)

        SettingsItem(
            text = stringResource(R.string.information_legal_terms_of_service),
            onClick = onTermsOfServiceClick,
            modifier = Modifier
        )

        SettingsItem(
            text = stringResource(R.string.information_legal_privacy_policy),
            onClick = onPrivacyPolicyClick,
            modifier = Modifier
        )

        if (state.isAuth) {
            HorizontalDivider(color = MaterialTheme.appColors.divider)

            SettingsLogoutButton(
                onClick = onLogoutClick,
                text = stringResource(R.string.settings_logout_button),
            )
        }

        Spacer(modifier = Modifier.height(16.dp).weight(1f))

        Text(
            text = state.versionInfo,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = Color(0xFF999999),
            modifier = modifier
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        Spacer(modifier = Modifier.navigationBarsPadding())
    }

}

@Preview
@Composable
fun SettingsContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        SettingsContent(
            state = SettingsState(versionInfo = "1.0.0 (1)", isAuth = true))
    }
}