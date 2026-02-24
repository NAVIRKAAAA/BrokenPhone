package com.broken.telephone.features.app_preferences.content

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.app_preferences.model.AppPreferencesState
import com.broken.telephone.features.edit_profile.content.AccountTextInfoItem
import com.broken.telephone.features.edit_profile.content.EditProfileTopBar
import com.broken.telephone.features.settings.content.SettingsItem

@Composable
fun AppPreferencesContent(
    state: AppPreferencesState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onThemeClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = stringResource(R.string.app_preferences_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsItem(
            text = stringResource(R.string.app_preferences_notifications),
            onClick = onNotificationsClick,
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        AccountTextInfoItem(
            name = stringResource(R.string.app_preferences_language),
            value = stringResource(state.language.displayNameResId),
            modifier = Modifier
                .clickable(onClick = onLanguageClick)
                .padding(horizontal = 16.dp),
        )

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        AccountTextInfoItem(
            name = stringResource(R.string.app_preferences_theme),
            value = stringResource(state.theme.displayNameResId),
            modifier = Modifier
                .clickable(onClick = onThemeClick)
                .padding(horizontal = 16.dp),
        )

    }
}

@Preview
@Composable
fun AppPreferencesContentPreview() {
    BrokenTelephoneTheme {
        AppPreferencesContent(state = AppPreferencesState())
    }
}
