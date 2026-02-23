package com.broken.telephone.features.app_preferences

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.app_preferences.content.AppPreferencesContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppPreferencesScreen(
    onBackClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onThemeClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AppPreferencesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AppPreferencesContent(
        state = state,
        onBackClick = onBackClick,
        onNotificationsClick = onNotificationsClick,
        onLanguageClick = onLanguageClick,
        onThemeClick = onThemeClick,
        modifier = modifier,
    )
}
