package com.broken.telephone.features.welcome.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary
            ),
            enabled = true,
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues()
        ) {
            Text(
                text = "Get Started",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color(0xFFF5F5F5)
            ),
            enabled = true,
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues()
        ) {
            Text(
                text = "Sign In",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        TextButton(
            onClick = onContinueAsGuest,
        ) {
            Text(
                text = "Continue as Guest",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.inter_medium)),
                fontSize = 16.sp,
                lineHeight = 22.sp,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val primaryColor = MaterialTheme.colorScheme.primary
        val termsText = buildAnnotatedString {
            append("By continuing, you agree to our ")
            withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.Underline)) {
                append("Terms")
            }
            append(" & ")
            withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.Underline)) {
                append("Privacy Policy")
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