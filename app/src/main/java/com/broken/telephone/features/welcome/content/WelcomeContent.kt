package com.broken.telephone.features.welcome.content

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.broken.telephone.R
import com.broken.telephone.core.theme.BrokenTelephoneTheme

@Composable
fun WelcomeContent(
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
            contentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        WelcomeButton(
            text = stringResource(R.string.welcome_sign_in),
            onClick = onSignIn,
            contentColor = Color.Black,
            containerColor = Color(0xFFF5F5F5),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))


        TextButton(
            onClick = onContinueAsGuest,
        ) {
            Text(
                text = stringResource(R.string.welcome_continue_as_guest),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 16.sp,
                lineHeight = 22.sp,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val primaryColor = MaterialTheme.colorScheme.primary
        val termsText = buildAnnotatedString {
            append("${stringResource(R.string.welcome_terms_prefix)} ")
            withStyle(SpanStyle(color = primaryColor)) {
                append(stringResource(R.string.welcome_terms))
            }
            append(" ${stringResource(R.string.welcome_terms_and)} ")
            withStyle(SpanStyle(color = primaryColor)) {
                append(stringResource(R.string.welcome_privacy_policy))
            }
        }

        Text(
            text = termsText,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.inter_regular)),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color(0xFF999999)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Preview
@Composable
fun WelcomeContentPreview() {
    BrokenTelephoneTheme() {
        WelcomeContent(onContinueAsGuest = {})
    }
}