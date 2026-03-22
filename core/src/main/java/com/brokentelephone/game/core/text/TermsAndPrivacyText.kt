package com.brokentelephone.game.core.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun TermsAndPrivacyText(
    prefix: String,
    termsText: String,
    andText: String,
    privacyPolicyText: String,
    onTermsClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    val text = buildAnnotatedString {
        append("$prefix ")
        withLink(
            LinkAnnotation.Clickable(
                tag = "TERMS",
                linkInteractionListener = { onTermsClick() },
            )
        ) {
            withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.None)) {
                append(termsText)
            }
        }
        append(" $andText ")
        withLink(
            LinkAnnotation.Clickable(
                tag = "PRIVACY",
                linkInteractionListener = { onPrivacyPolicyClick() },
            )
        ) {
            withStyle(SpanStyle(color = primaryColor, textDecoration = TextDecoration.None)) {
                append(privacyPolicyText)
            }
        }
    }

    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily(Font(R.font.nunito_regular)),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        modifier = modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview
@Composable
private fun TermsAndPrivacyTextPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        TermsAndPrivacyText(
            prefix = "By signing up, you agree to our",
            termsText = "Terms",
            andText = "&",
            privacyPolicyText = "Privacy Policy",
            onTermsClick = {},
            onPrivacyPolicyClick = {},
        )
    }
}
