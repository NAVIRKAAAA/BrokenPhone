package com.broken.telephone.features.theme.content

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.domain.settings.AppTheme
import com.broken.telephone.features.edit_profile.content.EditProfileTopBar
import com.broken.telephone.features.language.content.LanguageRadioItem
import com.broken.telephone.features.theme.model.ThemeState

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
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
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
