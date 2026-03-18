package com.brokentelephone.game.features.theme.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brokentelephone.game.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.appColors
import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.features.edit_profile.content.EditProfileTopBar
import com.brokentelephone.game.features.language.content.LanguageRadioItem
import com.brokentelephone.game.features.theme.model.ThemeState

@Composable
fun ThemeContent(
    state: ThemeState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onThemeClick: (AppTheme) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = stringResource(R.string.theme_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppTheme.entries.forEach { theme ->
            LanguageRadioItem(
                text = stringResource(theme.displayNameResId),
                selected = theme == state.selectedTheme,
                onClick = { onThemeClick(theme) },
                body = if (theme == AppTheme.SYSTEM) stringResource(R.string.theme_system_description) else null,
            )

            if (theme != AppTheme.entries.lastOrNull()) {
                HorizontalDivider(color = MaterialTheme.appColors.divider)
            }
        }
    }
}

@Preview
@Composable
fun ThemeContentPreview() {
    BrokenTelephoneTheme {
        ThemeContent(state = ThemeState())
    }
}
