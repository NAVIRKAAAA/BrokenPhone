package com.brokentelephone.game.features.language.content

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
import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.features.edit_profile.content.EditProfileTopBar
import com.brokentelephone.game.features.language.model.LanguageState

@Composable
fun LanguageContent(
    state: LanguageState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLanguageClick: (Language) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        EditProfileTopBar(
            title = stringResource(R.string.language_title),
            onBackClick = onBackClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Language.entries.forEach { language ->
            LanguageRadioItem(
                text = stringResource(language.displayNameResId),
                selected = language == state.selectedLanguage,
                onClick = { onLanguageClick(language) },
            )

            if(language != Language.entries.lastOrNull()) {
                HorizontalDivider(color = MaterialTheme.appColors.divider)
            }
        }
    }
}

@Preview
@Composable
fun LanguageContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        LanguageContent(state = LanguageState())
    }
}
