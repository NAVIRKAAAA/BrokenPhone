package com.brokentelephone.game.features.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.features.language.content.LanguageContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LanguageScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LanguageContent(
        state = state,
        onBackClick = onBackClick,
        onLanguageClick = viewModel::onLanguageClick,
        modifier = modifier,
    )
}
