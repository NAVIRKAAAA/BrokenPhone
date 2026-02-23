package com.broken.telephone.features.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.theme.content.ThemeContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ThemeScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ThemeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ThemeContent(
        state = state,
        onBackClick = onBackClick,
        onThemeClick = viewModel::onThemeClick,
        modifier = modifier,
    )
}
