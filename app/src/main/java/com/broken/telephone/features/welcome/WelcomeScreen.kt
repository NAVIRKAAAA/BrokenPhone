package com.broken.telephone.features.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.broken.telephone.features.welcome.content.WelcomeContent

@Composable
fun WelcomeScreen(
    onContinueAsGuest: () -> Unit,
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {},
    modifier: Modifier = Modifier,
) {

    WelcomeContent(
        onContinueAsGuest = onContinueAsGuest,
        onGetStarted = onGetStarted,
        onSignIn = onSignIn,
        modifier = modifier
    )

}
