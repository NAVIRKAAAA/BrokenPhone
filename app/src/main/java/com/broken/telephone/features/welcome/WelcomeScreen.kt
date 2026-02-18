package com.broken.telephone.features.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.broken.telephone.features.welcome.content.WelcomeContent

@Composable
fun WelcomeScreen(
    onContinueAsGuest: () -> Unit,
    modifier: Modifier = Modifier,
) {

    WelcomeContent(
        onContinueAsGuest = onContinueAsGuest,
        modifier = modifier
    )

}
