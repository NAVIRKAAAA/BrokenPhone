package com.broken.telephone.features.language.content

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
import com.broken.telephone.domain.settings.Language
import com.broken.telephone.features.edit_profile.content.EditProfileTopBar
import com.broken.telephone.features.language.model.LanguageState

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
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}

@Preview
@Composable
fun LanguageContentPreview() {
    BrokenTelephoneTheme {
        LanguageContent(state = LanguageState())
    }
}
