package com.brokentelephone.game.features.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.localization.LocaleUtils
import com.brokentelephone.game.domain.model.settings.toLocale
import com.brokentelephone.game.features.language.content.LanguageContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LanguageScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LanguageContent(
        state = state,
        onBackClick = onBackClick,
        onLanguageClick = { language ->
            viewModel.onLanguageClick(language)
            LocaleUtils.changeLanguage(context, language.toLocale())
        },
        modifier = modifier,
    )
}
