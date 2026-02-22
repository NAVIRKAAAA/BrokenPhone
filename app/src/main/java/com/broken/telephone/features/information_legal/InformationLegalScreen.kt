package com.broken.telephone.features.information_legal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.broken.telephone.core.browser.openCustomTab
import com.broken.telephone.features.information_legal.content.InformationLegalContent
import com.broken.telephone.features.information_legal.model.InformationLegalSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InformationLegalScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: InformationLegalViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is InformationLegalSideEffect.OpenLink -> context.openCustomTab(effect.url)
            }
        }
    }

    InformationLegalContent(
        onBackClick = onBackClick,
        onTermsOfServiceClick = viewModel::onTermsOfServiceClick,
        onPrivacyPolicyClick = viewModel::onPrivacyPolicyClick,
        modifier = modifier,
    )
}
