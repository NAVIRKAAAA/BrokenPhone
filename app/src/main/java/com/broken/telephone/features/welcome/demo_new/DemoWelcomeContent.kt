package com.broken.telephone.features.welcome.demo_new

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.welcome.content.WelcomeButton

@Composable
fun DemoWelcomeContent(
    onContinueAsGuest: () -> Unit,
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {},
    modifier: Modifier = Modifier
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
            onClick = onContinueAsGuest,
        ) {
            Text(
                text = stringResource(R.string.welcome_continue_as_guest),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                fontSize = 16.sp,
                lineHeight = 22.sp,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Preview
@Composable
fun DemoWelcomeContentPreview() {
    BrokenTelephoneTheme(
        darkTheme = true
    ) {
        DemoWelcomeContent(onContinueAsGuest = {})
    }
}