package com.brokentelephone.game.features.welcome.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.button.WelcomeButton
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.features.welcome.model.WelcomeState

@Composable
fun WelcomeContent(
    state: WelcomeState,
    modifier: Modifier = Modifier,
    onPlayAsGuestClick: () -> Unit,
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        WelcomeButton(
            text = stringResource(R.string.welcome_get_started),
            onClick = onGetStarted,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        WelcomeButton(
            text = stringResource(R.string.welcome_sign_in),
            onClick = onSignIn,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onPlayAsGuestClick,
            enabled = !state.isLoading,
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = LocalContentColor.current,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = stringResource(R.string.welcome_continue_as_guest),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun WelcomeContentPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        WelcomeContent(
            state = WelcomeState(isLoading = false),
            onPlayAsGuestClick = {}
        )
    }
}
