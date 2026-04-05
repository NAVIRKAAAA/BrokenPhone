package com.brokentelephone.game.features.account_settings.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@Composable
fun AccountEmailInfoItem(
    email: String,
    isVerified: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.account_settings_email),
                fontFamily = FontFamily(Font(R.font.nunito_semi_bold)),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = email,
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    textAlign = TextAlign.End,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
            }
        }

        Text(
            text = if (isVerified) {
                stringResource(R.string.account_settings_email_verified)
            } else {
                stringResource(R.string.account_settings_email_not_verified)
            },
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontSize = 13.sp,
            lineHeight = 19.sp,
            color = if (isVerified) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.error
            },
            modifier = Modifier.padding(bottom = 16.dp),
        )
    }
}

@Preview
@Composable
fun AccountEmailInfoItemVerifiedPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            AccountEmailInfoItem(
                email = "alex@example.com",
                isVerified = true,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview
@Composable
fun AccountEmailInfoItemNotVerifiedPreview() {
    BrokenTelephoneTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            AccountEmailInfoItem(
                email = "alex@example.com",
                isVerified = false,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}
